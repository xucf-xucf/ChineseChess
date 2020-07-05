package com.ChineseChess.control.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dbSource.base.service.BaseService;

@RequestMapping("file")
@Controller
public class file extends BaseService{
    private static final String CHESS_PALCES = "一二三四五六七八123456789";
	private static final String CHESS_ACTIONS = "进平退";
	private static final String CHESS_NAMES = "兵卒车車马炮士相象帅将前后中一二三四";
	/**
     * 文件上传功能
     * @param file
     * @return
     * @throws IOException 
     */
    @RequestMapping(value="/upload",method=RequestMethod.POST,produces="text/plain;charset=UTF-8")
    @ResponseBody
    public String upload(MultipartFile file,HttpServletRequest request,Model model) throws IOException{
    	
        String path = request.getSession().getServletContext().getRealPath("upload");
        String fileName = file.getOriginalFilename();  
        File dir = new File(path,fileName);        
        if(!dir.exists()){
            dir.mkdirs();
        }
        file.transferTo(dir);
        InputStream inputStream = new FileInputStream(dir);
        byte[] bs = new byte[(int)dir.length()];
        inputStream.read(bs);
        inputStream.close();
        String CCStr = new String(bs,"GBK");
        List<String>list = formatChessInfo(CCStr);
        StringBuffer buffer = new StringBuffer();
        for(String s:list){
        	buffer.append(s+"<br>");
        }
        model.addAttribute("info", buffer.toString());
        return buffer.toString();
    }
    
    /**
     * 文件下载功能
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/down")
    public void down(HttpServletRequest request,HttpServletResponse response) throws Exception{
        //模拟文件，myfile.txt为需要下载的文件
        String fileName = request.getSession().getServletContext().getRealPath("upload")+"/cs.txt";
        //获取输入流
        InputStream bis = new BufferedInputStream(new FileInputStream(new File("F:\\cs.txt")));
        //假如以中文名下载的话
        String filename = "下载文件.txt";
        //转码，免得文件名中文乱码
        filename = URLEncoder.encode("F:\\cs.txt","UTF-8");
        //设置文件下载头
        response.addHeader("Content-Disposition", "attachment;filename=" + filename);  
        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型  
        response.setContentType("multipart/form-data"); 
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        int len = 0;
        while((len = bis.read()) != -1){
            out.write(len);
            out.flush();
        }
        out.close();
    }
    public static String[] judgeSplitStr(String str){
    	return judgeSplitStr(str,"");
    }
    /**
     * 计算单行规则字符的分隔符
     * @param str
     * @return
     */
    public static String[] judgeSplitStr(String str,String special){
    	String[] strs=new String[2];
    	str = formatStr(str);
    	strs[1] = str;
    	char[] charArr = str.toCharArray();
    	boolean flag=true;
    	boolean flag2=false;
    	for(int i=str.length()/2;i<str.length();i++){
    		String spTemp = String.valueOf(charArr[i]);
    		if(spTemp.equals(special))continue;
    		String[] tempStrs = str.split(spTemp);
    		int len = tempStrs.length/2;
    		int count=0;
    		int tempI=0;
    		int countSp = 1;
    		int isMore = 0;
    		if(len<2)continue;
    		for(int j=0;j<tempStrs.length;j++){
    			if("".equals(tempStrs[j])&&tempStrs[j].length()<1){
    				isMore=1;
    				count++;
    				if(flag){
    					if(tempI==j-1){
    						countSp++;
    						flag2=true;
    					}else{
    						if(flag2){
    							if(j-countSp>5){
    								judgeSplitStr(str.substring(countSp));
    							}
    							flag = false;
    						}
    					}
    				}
    				tempI = j;
    			}
    			if(count>str.length()/8-1){
    				String sp = "";
    				for(int k=0;k<countSp+isMore-1;k++){
    					sp+=String.valueOf(charArr[i]);
    				}
    				if(str.split(sp)[str.split(sp).length/2].length()!=4){
    					strs[0] = judgeSplitStr(str.replace(sp, ""))[0];
    					return strs;
    				}
    				strs[0]=sp;
    				return strs;
    			}
    		}
    		if(tempStrs[len+1].replace(spTemp, "").length()
    				==tempStrs[len-1].replace(spTemp, "").length()
    				&&tempStrs[len+1].replace(spTemp, "").length()
    				==tempStrs[len].replace(spTemp, "").length()){
    			if(tempStrs[len].replace(spTemp, "").length()>4){
    				strs[0] = judgeSplitStr(str.replace(spTemp, CHESSINFO_SPLIT_STR))[0];
    				strs[1] = str.replace(spTemp, CHESSINFO_SPLIT_STR);
    				return strs;
    			}
    			strs[0]= spTemp;
    			return strs;
    		}
    	}
    	return strs;
    }
    
    /**
     * 针对换行，空格，制表符等的格式化处理
     * @param str
     * @return
     */
    public static String formatStr(String str){
    	str = str.replaceAll("\\s+", "\t").replaceAll("\r\n", "\t").replaceAll("\n", "\t").replaceAll("\r", "\t");
    	String[] ss1=  str.split("\\s+");
    	int len = ss1.length;
    	int index = 0;
        if(ss1.length>4){
        	StringBuffer buffer = new StringBuffer();
        	for(int i=0;i<len;i++){
        		if(i>len-2){
        			break;
        		}
        		//连续三行长度相等则很有可能进入正题
        		String ss12 = ss1[i+2];
				String ss11 = ss1[i+1];
				String ss10 = ss1[i];
				if(ss10.length()==ss11.length()&&ss11.length()==ss12.length()){
        				String temp1 = ss10;
        				String temp2 = ss11;
        				if(checkChase(temp1,temp2)){//确定进入正题
        					//找到第一个棋相关的
        					char[] s1s = temp1.toCharArray();
        					for(int in=0;in<s1s.length;in++){
        						String s1st = String.valueOf(s1s[in]);
        						if(CHESS_NAMES.contains(s1st)){
        							break;
        						}else{
        							index++;
        						}
        					}
        					break;
        				}
        		}
        		//隔行长度相等则很有可能进入正题
        		if(i<1){
        			continue;
        		}
        		String ss1_1 = ss1[i-1];
				if(ss10.length()==ss12.length()&&ss1_1.length()==ss11.length()&&
        				ss10.length()!=ss11.length()){
        			String temp1 = ss10.length()>ss11.length()?ss10:ss11;
        			String temp2 = ss1_1.length()>ss12.length()?ss1_1:ss12;
        			if(checkChase(temp1,temp2)){//确定进入正题
        				//找到第一个棋相关的
        				char[] s1s = temp1.toCharArray();
        				for(int in=0;in<s1s.length;in++){
        					String s1st = String.valueOf(s1s[in]);
        					if(CHESS_NAMES.contains(s1st)){
        						break;
        					}else{
        						index++;
        					}
        				}
        				break;
        			}
        		}
        	}
	       	for(String s:ss1){
	       		
	       		if(s.length()==4&&CHESS_NAMES.contains(s.substring(0,1))&&CHESS_ACTIONS.contains(s.substring(2,3))){
	       			buffer.append(s+CHESSINFO_SPLIT_STR);
	       		}else if(!CHESS_NAMES.contains(s.substring(index,index+1))){
	       			continue;
	       		}else{
	       			buffer.append(s.substring(index)+CHESSINFO_SPLIT_STR);
	       		}
	       	}
       	str = buffer.toString();
        }
        return str;
    }
    
    /**
     * 分析是否进入正题
     * @return
     */
    private static boolean checkChase(String s1,String s2){
    	char[] s1s = s1.toCharArray();
    	char[] s2s = s2.toCharArray();
    	for(int i=0;i<s1s.length-1;i++){
    		String s1st = String.valueOf(s1s[i]);
    		String s2st = String.valueOf(s2s[i]);
    		if(CHESS_ACTIONS.contains(s1st)&&CHESS_ACTIONS.contains(s2st)){
    			if(CHESS_PALCES.contains(String.valueOf(s1s[i+1]))&&CHESS_PALCES.contains(String.valueOf(s2s[i+1]))){
    				if(CHESS_PALCES.contains(String.valueOf(s1s[i-1]))&&CHESS_PALCES.contains(String.valueOf(s2s[i-1]))){
        				return true;
        			}
    			}
    		}
    	}
    	return false;
    }
    /**
     * 格式化棋谱
     * @param str
     * @return
     */
    public static List<String> formatChessInfo(String str){
    	
    	String[] sp2 = judgeSplitStr(str);
    	String[] temp = sp2[1].split(CHESSINFO_SPLIT_STR);
    	List<String> infoList = new ArrayList<String>();
    	for(String s:temp){
    		if(s.length()==4){
    			infoList.add(s);
    		}else{
    			if(s.length()<4){
    				continue;
    			}
    			String tstr = s.substring(s.length()-4);
    			char[] charArr = tstr.toCharArray();
    			if(CHESS_NAMES.contains(String.valueOf(charArr[0]))){
    				if(CHESS_ACTIONS.contains(String.valueOf(charArr[2]))){
    					infoList.add(tstr);
    				}
    			}
    				
    		}
    	}
    	return infoList;
    }
    public static void main(String[] args) throws Exception {
    	
    	
    	
    	System.out.println("==");
		if(false)
		return;
		System.out.println("--");
    	
    	/*List<String>list2 = new ArrayList<String>();
     	list2.add("C:\\Users\\Administrator\\Desktop\\cs.txt");
    	for(String s2 :list2){
    		
    	File dir = new File(s2);
    	 InputStream inputStream = new FileInputStream(dir);
         byte[] bs = new byte[(int)dir.length()];
         inputStream.read(bs);
         inputStream.close();
         String CCStr = new String(bs,"GBK");
         System.out.println("----------文件内容---------");
         System.out.println(CCStr);
        
         System.out.println("---------格式化结果-----------");
         List<String>list = formatChessInfo(CCStr);
         for(String s:list){
        	 System.out.println(s);
         }
    }*/
    }
    
    
    
    
    
    
    
    
}