package com.webSocket;

import java.util.ArrayList;
import java.util.List;

import com.ChineseChess.model.OnMove;

import net.sf.json.JSONArray;

/**
 *  中国象棋的记谱方法一般由四个字组成
 *	第1字是棋子的名称。如“马”或“車”。 </br>
 *  第2字是棋子所在纵线的数码。 </br>
 *	第3字表示棋子移动的方向：横走用“平”，向前走用“进”或“上”，向后走用“退”或“下”。 </br>
 *	第4字是棋子进退的格数，或者到达纵线的数码。
 *	<p>举例：</br>	
 *	“炮二平五”，表示红炮从纵线二平移到纵线五。 </br>
 *	“马8进7”，表示黑马从纵线8向前走到纵线7。</br>
 *	“車2退3”，表示黑車沿纵线2向后移动3格</br>
 *	当一方有2个以上名称相同的棋子位于同一纵线时，需要用“前”或“后”来加以区别。例如，“前马退六”（表示前面的红马退到直线六）、
 *	“后炮平4”（表示后面的黑炮平移到直线4）</br>
 *	<p>兵卒在特殊局面下的记谱方法(主要创作排局时遇到的多，实战也可遇到)</br>
 *	当兵卒在同一纵线达到3个，用前中后来区分，达到4个，用前二三四区分，达到5个，用前二三四五区分．</br>
 *	★★★★★当兵卒在两个纵线都达到两个以上时，按照旧的记谱方式举例：前兵九平八，此时可省略兵（卒），记做前九平八，
 *	以达到都用4个汉字记谱的要求，此表示方式已在中国象棋DhtmlXQ动态棋盘上实现，是对中文记谱方法的一个重要完善．</br>
 *  兵1 炮2 車3 马4 相5 士6 将7 红
 * @author 天理
 *
 */
public class CCUtil {
	public final static String CHESS_INITIAL_MAP = "[[-3,-4,-5,-6,-7,-6,-5,-4,-3],[0,0,0,0,0,0,0,0,0],[0,-2,0,0,0,0,0,-2,0],[-1,0,-1,0,-1,0,-1,0,-1],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[1,0,1,0,1,0,1,0,1],[0,2,0,0,0,0,0,2,0],[0,0,0,0,0,0,0,0,0],[3,4,5,6,7,6,5,4,3]]";
	public final static JSONArray CHESS_INITIAL_JSONARRAY = JSONArray.fromObject(CHESS_INITIAL_MAP);
	private final static String CHESS_COLOR_RED = "R";//红旗
	private final static String CHESS_COLOR_BLACK = "B";//红旗
	private final static String CENTRE_CHESS = "中";//中位置的子
	private static final String FORMER_CHESS = "前";//前位置的子
	private static final String REAR_CHESS = "后";//后位置的子
	private final static String CHESS_ACTION_ADVANCE = "进";//前进动作
	private final static String CHESS_ACTION_QUIT = "退";//后退动作
	private final static String CHESS_ACTION_HORIZONTAL = "平";//平移动作
	public static void main(String[] args) {
		String chess = "兵卒車马炮士相象帅将";
		for(int i=0;i<chess.length();i++){
			System.out.println(chess.charAt(i));
		}
		String color = "B";
		//		String color = "R";
		String enCodeCC = "炮8进4";
		//		String enCodeCC = "马八进七";
		//		String enCodeCC = "相三进一";
		//		String enCodeCC = "象7进5";
		//		String enCodeCC = "炮二平五";
		CCUtil cu = new CCUtil();
		System.out.println(cu.deCodeCC(enCodeCC, color, CHESS_INITIAL_JSONARRAY));
	}

	/**
	 * 将OnMove对象转中文传统叫法，基本完成，case也许会遗漏，但最难的都解决的害怕什么呢
	 * @param om
	 * @return 叫法
	 */
	public String enCodeCC(OnMove om){
		//EnCodeMove ecm = new EnCodeMove();
		String result = "";//结果
		String chessName = om.getChess();//棋子名
		//ecm.setChessName(chessName);
		String color = om.getColor();//红黑RB
		int y = stringToNumber(om.getBeforeY());//原y
		int x = stringToNumber(om.getBeforeX());//原x
		int j = stringToNumber(om.getAfterY());//后y
		int i = stringToNumber(om.getAfterX());//后x
		//相对棋盘来说要+1
		int ChessY = y+1;
		int ChessX = x+1;
		int ChessJ = j+1;
		int ChessI = i+1;
		//加一后，后续关于数组的操作全都加一
		String beforeX = ""; //原始位置
		if(color.equals(CHESS_COLOR_RED)){//红方
			beforeX = enCodeNum(10-ChessX); 
		}else{
			beforeX = Integer.toString(ChessX);//坐标系从0开始，棋盘从1开始
		}
		String specialObiit = beforeX;
		JSONArray jsonArray = JSONArray.fromObject(om.getMap());//当前局面
		int[][] array = JSONArrayToIntArray(jsonArray);
		//返回上一步棋子状态
		array[y][x] = array[j][i];
		array[j][i] = 0;//未必是0，但是暂时没影响，主要防止防止对yx位置的影响，后续做吃子处理
		int chessNum = array[y][x];
		int onYNum = onYChessCnt(array,chessNum,x);

		String prefix = "";//前缀
		String action = "";//动作
		String displacement = "";//y位移
		action = judgeAction(color, x, y, i, j);//动作
		displacement = judgeDisplacement(color, x, y, i, j);
		int chessValue = absoluteValue(chessNum);//棋子绝对值
		if(onYNum>1){//一条线上有重叠的
			beforeX = "";
			boolean flag = false;
			int arr[] = getOverlapYArray(array,chessNum,x);//纵轴数组，从上往下方的
			if(chessValue == 1&&(onYNum==2||onYNum==3)){//兵卒会不会遇到超级特殊的情况，两两一列
				flag = judgeSpecial(x, array, chessNum, onYNum);
			}
			if(flag){//兵卒的两两或两三一列
				chessName = specialObiit;
				if(onYNum==2){
					int Y = getOverlapYOnTow(arr,y);//另一子的纵坐标
					prefix = judgePrefix(color,Y,y);//前缀
				}else if(onYNum==3){
					prefix = judgePrefixByThree(arr, y, color);
				}
			}else if(chessValue<4||chessValue==7){//兵卒炮車帅将处理
				//				action = judgeAction(color, x, y, i, j);//动作
				//				displacement = judgeDisplacement(color, x, y, i, j);
				if(onYNum == 2){//两个
					int Y = getOverlapYOnTow(arr,y);//另一子的纵坐标
					prefix = judgePrefix(color,Y,y);//前缀
				}else if(onYNum == 3){//同一纵列有三个
					prefix = judgePrefixByThree(arr, y, color);
				}else if(onYNum == 4){//同一纵列有三个
					prefix = judgePrefixByMore(arr, y, color, onYNum);
				}else if(onYNum == 5){
					prefix = judgePrefixByMore(arr, y, color, onYNum);
				}
			}else if(chessValue<7&&chessValue>3){//马象士
				int Y = getOverlapYOnTow(arr,y);//另一子的纵坐标
				prefix = judgePrefix(color,Y,y);//前缀
				//				action = judgeAction(color, x, y, i, j);//动作
				//				displacement = judgeDisplacement(color, x, y, i, j);
			}
		}else{//无重叠的
			if(absoluteValue(chessNum)==1){//兵卒处理
				//				action = judgeAction(color, x, y, i, j);//动作
				//				displacement = judgeDisplacement(color, x, y, i, j);
			}
		}
		result+=prefix+chessName+beforeX+action+displacement;
		om.setEncodeMsg(result);
		return result;
	}

	/**
	 * 对兵卒的特殊情况处理,判断是否出现了两两一列或两三一列的情况
	 * @param x 初始x
	 * @param array 局面数组
	 * @param chessNum 棋子代号
	 * @param onYNum 重叠子数目
	 * @return
	 */
	private boolean judgeSpecial(int x, int[][] array, int chessNum, int onYNum) {
		for(int _i=0;_i<9;_i++){//全局去找另外两个或者三个棋子
			if(_i==x)continue;
			for(int _j=0;_j<10;_j++){
				if(array[_j][_i]==chessNum){//在某xy上找到棋子
					int temp = onYChessCnt(array,chessNum,_i);//查看这条线上重叠子
					if(temp == 1){//才浮现3个
						if(onYNum == 2){//已经重叠的有两个，则还需要找到另外两个
							for(int __i=_i;__i<9;__i++){//全局去找另外两个个棋子
								if(__i==x)continue;
								for(int __j=0;__j<10;__j++){
									if(array[__j][__i]==chessNum){
										int tempX = onYChessCnt(array,chessNum,__i);//查看这条线上重叠子
										if(tempX >1){//超特殊情况
											return true;
										}
									}
								}
							}
						}
					}else if(temp>1){//超特殊的情况
						return true;
					}
				}
			}
		}
		return false;
	}



	/**
	 * 将中文转化为OnMove对象，需要局面数据
	 * @param enCodeCC
	 * @param color
	 * @param jsonArray 局面数据（局面数据为动作前的）
	 * @return 返回om对象，其中map为动作之后的局面
	 */
	public OnMove deCodeCC(String enCodeCC,String color,JSONArray jsonArray){

		int[][] array = JSONArrayToIntArray(jsonArray);
		String chess = "兵車卒车马炮士相象帅将";
		List<String> list = StringToList(enCodeCC);
		String chessNum = "";//棋子代号
		String chessName = "";//棋子名
		String chessPlace = "";//棋子初始位置
		String chessAction = list.get(list.size()-2);//动作,肯定是最后第二个位置的字
		String displacement = list.get(list.size()-1);//位移,肯定是最后一个位置的字
		if(color == null){

		}
		if(!"123456789".contains(displacement)){
			displacement = numberToString(deCodeNum(displacement));
		}

		OnMove om = new OnMove();
		int num = 0;
		//		if(color.equals("R")){//红//兵1 炮2 車3 马4 相5 士6 将7 红
		String xyij[] = new String[4];
		if(chess.contains(list.get(0))){//第一字为棋子，则为常规状态
			chessName = list.get(0);
			chessNum = chessNameToNum(chessName,color);
			num = stringToNumber(chessNum);
			chessPlace = list.get(1);//这个位置是棋盘的位置，到数组里面要+1
			xyij = doDeCodeNormal(array,chessNum,chessPlace,chessAction,displacement);
		}else{//第一字不是棋子，则出现叠子情况
			chessName = list.get(1);
			chessNum = chessNameToNum(chessName,color);
			num = stringToNumber(chessNum);
			om.setChess(chessNum);
			chessPlace = list.get(0);//前兵或者啥的//三个的兵暂时不考虑
			int x = 0;
			int y = 0;
			if(chess.contains(chessName)&&!"兵卒".contains(chessName)){//前后棋子
				outer:	for(int _j=0;_j<9;_j++){
					for(int _i=0;_i<10;_i++){
						if(array[_i][_j]==num){//在某xy上找到棋子
							//找到初始位置
							if((color.equals(CHESS_COLOR_RED)&&chessPlace.equals(FORMER_CHESS))
									||(color.equals(CHESS_COLOR_BLACK)&&chessPlace.equals(REAR_CHESS))){//如此从小到大找到
								x = _j;
								y = _i;
							}else{
								for(int __i=_i+1;__i<10;__i++){
									if(array[__i][_j]==num){//在某xy上找到棋子
										x=_j;
										y=__i;
										break;
									}
								}
							}
							xyij=displayOnMove(chessNum, chessAction, displacement, y, x);
							break outer;
						}
					}
				}
			}else{//三个卒的情况了，不好处理哦
				if(chess.contains(list.get(1))){//第二个字为兵或者卒，说明不是两两一列的或三两一列的情景
					String prefix = list.get(0);
					int xys[] = getXYsPlace(color, array, prefix);	
					num = judgeDivisor(color)*1;//棋子代号
					xyij=displayOnMove(numToString(num), chessAction, displacement, xys[1], xys[0]);
				}else{//两两一列或三两一列的情况，前二平四，中3进1
					String prefix = list.get(0);//只有前中后三个情况
					String prefixX = list.get(1);//第二子为x的位置
					if(CHESS_COLOR_RED.equals(color)){//红旗
						chessName = "兵";
						num =1;
						x = 9-deCodeNum(prefixX);
						int arr[] = getOverlapYArray(array,num,x);//纵轴数组，从上往下方的
						if("前".equals(prefix)){
							y = arr[0];
						}else if("中".equals(prefix)){
							y = arr[1];
						}else if("后".equals(prefix)){
							y = arr[arr.length-1];
						}
					}else{//黑棋
						chessName = "卒";
						num = -1;
						x = stringToNumber(prefixX)-1;
						int arr[] = getOverlapYArray(array,num,x);//纵轴数组，从上往下方的
						if("前".equals(prefix)){
							y = arr[arr.length-1];
						}else if("中".equals(prefix)){
							y = arr[1];
						}else if("后".equals(prefix)){
							y = arr[0];
						}
					}
					xyij=displayOnMove(numToString(num), chessAction, displacement, y, x);
				}
			}	
		}
		om.setBeforeX(xyij[0]);
		om.setBeforeY(xyij[1]);
		om.setAfterX(xyij[2]);
		om.setAfterY(xyij[3]);
		om.setChess(chessNum);
		array[stringToNumber(xyij[1])][stringToNumber(xyij[0])] = 0;
		array[stringToNumber(xyij[3])][stringToNumber(xyij[2])] = num;
		jsonArray = IntArrayToJSONArray(array);
		om.setMap(jsonArray);
		return om;
	}

	private String numToString(int num) {
		return num+"";
	}

	/**
	 * 找到棋子的初始位置的位置
	 * @param color
	 * @param array
	 * @param prefix
	 * @return
	 */
	private int[] getXYsPlace(String color, int[][] array, String prefix ) {
		int num = judgeDivisor(color)*1;//棋子代号
		int[] xys = new int[2];
		int xy[] = findSpacelChess(array, num, -1);//叠子所在x的第一个棋子
		xys[0] = xy[0];
		int onYNum = onYChessCnt(array,num,xy[0]);//本x的叠子数目
		if(CHESS_COLOR_RED.equals(color)){//红旗
			if("前".equals(prefix)){//对红棋来讲，前就是叠字中y最小的那个
				xys[1] = xy[1];
			}else if("二三四中".contains(prefix)){
				xys[1] = findYBySpacelX(array, num, xy[0], deCodeNum(prefix));
			}else if("后".equals(prefix)){
				xys[1] = findYBySpacelX(array, num, xy[0], onYNum);
			}
		}else{//黑棋
			if("前".equals(prefix)){
				xys[1] = findYBySpacelX(array, num, xy[0], onYNum);
			}else if("二三四中".contains(prefix)){
				xys[1] = findYBySpacelX(array, num, xy[0], onYNum-deCodeNum(prefix));
			}else if("后".equals(prefix)){
				xys[1] = xy[1];
			}
		}
		return xys;
	}

	/**
	 * 重叠x上查找指定位置的y
	 * @param array 局面
	 * @param num 棋子代号
	 * @param x 棋子x
	 * @param count 指定位置（自上二下）
	 * @return
	 */
	public int findYBySpacelX(int[][] array, int num, int x,int count){
		int tempCount = 0;
		for(int _y=0;_y<10;_y++){
			if(array[_y][x]==num){
				tempCount++;
			}
			if(tempCount == count){
				return _y;
			}
		}
		return 0;
	}
	
	/**
	 * 全局寻找，叠子兵所在的x和第一个的y
	 * @param array 局面
	 * @param num 棋子代号
	 * @param _j 从0开始则输入-1
	 * @return
	 */
	private int[] findSpacelChess(int[][] array, int num, int _j) {
		int onYNum;
		int xy[] = new int[2];
		for(int __j=_j+1;__j<9;__j++){
			for(int __i=0;__i<10;__i++){
				if(array[__i][__j]==num){//第一次找到这个兵
					onYNum = onYChessCnt(array,num,__j);
					if(onYNum>1){//初始位置找到
						xy[0] = __j;
						xy[1] = __i;
						return xy;
					}else{//在这条x后继续找下去
						return findSpacelChess(array,num,__j);
					}
				}
			}
		}
		return null;
	}
	/**
	 * 写到om里,适合正常情况
	 * @param array
	 * @param chessNum 棋子代号
	 * @param chessPlace 棋子位置
	 * @param chessAction 动作
	 * @param displacement 位移
	 * @return String[] xyij
	 */
	private String[] doDeCodeNormal(int[][] array, String chessNum, String chessPlace, 
			String chessAction,String displacement) {
		int y = 0; //初始y
		int x = 0; //初始x
		int j = 0; //位移后的y
		int i = 0; //位移后的i
		int num = stringToNumber(chessNum);

		if(!"123456789".contains(chessPlace)){//统一用数字表示位置,判断为红旗
			chessPlace = numberToString(9-deCodeNum(chessPlace));
		}else{
			chessPlace = numberToString(stringToNumber(chessPlace)-1);
		}
		//在棋局上找到初始位置
		int chessX = stringToNumber(chessPlace);
		for(int _j=0;_j<10;_j++){
			if(array[_j][chessX]==num){//在某x上找到y了
				x=chessX;//棋子初始位置找到
				y=_j;
				break;
			}
		}
		//直线的炮車卒将帅  1237
		//非直线的马像士 456
		return displayOnMove(chessNum, chessAction, displacement, y, x);
	}

	/**
	 * 位移操作转意
	 * @param chessNum
	 * @param chessAction
	 * @param displacement
	 * @param y
	 * @param x
	 * @return
	 */
	private String[] displayOnMove(String chessNum, String chessAction, String displacement,int y,
			int x) {
		String[] resStr=new String[4];
		int j;
		int i;
		int num = stringToNumber(chessNum);
		int displacementNum = stringToNumber(displacement);
		if("1237-1-2-3-7".contains(chessNum)){//直线系列棋子
			if(chessAction.equals(CHESS_ACTION_HORIZONTAL)){//平
				i = getAfterXByLineChess(num, displacementNum);
				j=y;
			}else{//进退
				j = innerOptLineChess(y,num,chessAction,displacementNum);
				i=x;
			}
		}else{//非直线系列棋子，其数字表示x的位置，不再是位移
			if(num>0){//红旗
				i = 9-displacementNum;
			}else{
				i = displacementNum-1;
			}
			//			i = getAfterXBydisplacementNum(num, displacementNum);
			j = getAfterYByOpt(chessNum, chessAction, y, x, i);
		}
		resStr[0]=x+"";
		resStr[1]=y+"";
		resStr[2]=i+"";
		resStr[3]=j+"";
		return resStr;
	}

	/**
	 * 直线系棋子
	 * @param num 棋子代号
	 * @param displacementNum 位移
	 * @return
	 */
	private int getAfterXByLineChess(int num, int displacementNum) {
		if(num>0){//红旗，
			return 9-displacementNum;
		}else{
			return displacementNum-1;
		}
	}


	/**
	 * 细节操作，获取移动后的y
	 * @param chessNum 棋子代号
	 * @param chessAction 动作
	 * @param y 初始y
	 * @param x 初始x
	 * @param i 位移后的x
	 * @return
	 */
	private int getAfterYByOpt(String chessNum, String chessAction, int y, int x, int i) {
		if("6-6".contains(chessNum)){//士
			return innerOptShi(y,chessNum,chessAction);
		}else if("4-4".contains(chessNum)){//马
			return innerOptMa(y,i,x,chessNum,chessAction);
		}else{//相
			return innerOptXiang(y,chessNum,chessAction);
		}
	}


	/**
	 * 内部细节操作，获取直行方式的y
	 * @param y 初始y
	 * @param num 棋子代号
	 * @param chessAction 动作
	 * @param displacementNum 位移
	 * @return
	 */
	private int innerOptLineChess(int y,int num,String chessAction,int displacementNum){
		int divisor = getDivisor(0>num?"-1":"1", chessAction);
		return y+displacementNum*divisor;
	}

	/**
	 * 内部操作,关于马
	 * @param y
	 * @param i
	 * @param x
	 * @param chessNum
	 * @param chessAction
	 * @return
	 */
	private int innerOptMa(int y,int i,int x,String chessNum,String chessAction){
		int divisor = getDivisor(chessNum, chessAction);
		if(absoluteValue(i-x)>1){//x的位置变话超过1为倒日进
			return y+1*divisor;
		}else{
			return y+2*divisor;
		}
	}

	/**
	 * 内部操作，计算因子
	 * <p>
	 * 红旗退 返回 1 ，其他的同理*-1
	 * @param chessNum 棋子代号
	 * @param chessAction 动作
	 * @return
	 */
	private int getDivisor(String chessNum, String chessAction) {
		int divisor = 1;
		if(!chessNum.contains("-")){
			divisor = -1;		
		}
		if(chessAction.equals(CHESS_ACTION_QUIT)){
			divisor = divisor*-1;
		}
		return divisor;
	}

	/**
	 * 内部操作，士
	 * @param y
	 * @param chessNum
	 * @param chessAction
	 * @return
	 */
	private int innerOptShi(int y,String chessNum,String chessAction){
		int divisor = getDivisor(chessNum, chessAction);
		return y+1*divisor;
	}

	/**
	 * 内部操作，象
	 * @param y
	 * @param chessNum
	 * @param chessAction
	 * @return
	 */
	private int innerOptXiang(int y,String chessNum,String chessAction){
		int divisor = getDivisor(chessNum, chessAction);
		return y+2*divisor;
	}

	/**
	 * 将字符串，一一放入list
	 * @param str
	 * @return
	 */
	private List<String> StringToList(String str){
		List<String> list = new ArrayList<String>();
		for(int i=0;i<str.length();i++){
			list.add(String.valueOf(str.charAt(i)));
		}
		return list;
	}

	/**	
	 * 兵1 炮2 車3 马4 相5 士6 帅7 红
	 * 棋子名转字符数字
	 * @param chessName
	 * @param color
	 * @return
	 */
	private String chessNameToNum(String chessName,String color){
		int temp=-1;
		if(color.equals(CHESS_COLOR_RED)){
			temp=1;
		}
		int i = 0;
		switch(chessName)
		{
		case "兵":
			i = 1;
			break;
		case "卒":
			i=-1;
			break;
		case "炮":
			i=temp*2;
			break;
		case "車":
			i=3*temp;
			break;
		case "车":
			i=3*temp;
			break;
		case "马":
			i=4*temp;
			break;
		case "象":
			i=-5;
			break;
		case "相":
			i=5;
			break;
		case "士":
			i=temp*6;
			break;
		case "将":
			i=-7;
			break;
		case "帅":
			i=7;
			break;
		}
		return numberToString(i);
	}

	/**
	 * 将数字转化为中文的方式
	 * @param num
	 * @return
	 */
	private String enCodeNum(int num){
		switch(num)
		{	
		case 1:
			return "一";
		case 2:
			return "二";
		case 3:
			return "三";
		case 4:
			return "四";
		case 5:
			return "五";
		case 6:
			return "六";
		case 7:
			return "七";
		case 8:
			return "八";
		case 9:
			return "九";
		default :
			return Integer.toString(num);
		}
	}

	/**
	 * 将中文转化为数字的方式
	 * @param num
	 * @return
	 */
	private  int deCodeNum(String num){
		switch(num)
		{	
		case "一":
			return 1;
		case "二":
			return 2;
		case "中":
			return 2;
		case "三":
			return 3;
		case "四":
			return 4;
		case "五":
			return 5;
		case "六":
			return 6;
		case "七":
			return 7;
		case "八":
			return 8;
		case "九":
			return 9;
		default :
			return 0;
		}
	}

	/**
	 * 将字符数字转化为int
	 * @param num
	 * @return
	 */
	private  int stringToNumber(String num){
		return Integer.parseInt(num);
	}

	/**
	 * 将数字转化为string
	 * @param num
	 * @return
	 */
	private  String numberToString(int num){
		return Integer.toString(num);
	}


	/**
	 * 将棋局转换为二维int数组
	 * @param jsonArray
	 * @return int[][]
	 */
	private  int[][] JSONArrayToIntArray(JSONArray jsonArray){
		int[][] array = new int[10][9];
		for(int i=0;i<jsonArray.size();i++){
			String arrayString = jsonArray.getString(i).toString();
			String[] stringArray = arrayString.split(",");
			int len = stringArray.length;
			for(int j=0;j<len;j++){
				String str = stringArray[j];
				str = str.replace("[", "").replace("]", "");
				array[i][j] = Integer.parseInt(str);
			}
		}
		return array;
	}

	/**
	 * int二维数组转化JSONArray
	 * @param array
	 * @return
	 */
	private  JSONArray IntArrayToJSONArray(int[][] array){
		JSONArray jsonMap2 = new JSONArray();
		jsonMap2.add(array);
		return (JSONArray)jsonMap2.get(0);
	}

	/**
	 * 一条纵线上相同棋子数
	 * @param array
	 * @param chessNum
	 * @param x
	 * @return
	 */
	private  int onYChessCnt(int[][]array,int chessNum,int x){
		int count = 0;
		for(int i=0;i<10;i++){
			if(chessNum == array[i][x]){
				count++;
			}
		}
		return count;
	}

	/**
	 * 重叠子为两个的纵坐标
	 * @param array
	 * @param chessNum
	 * @param x
	 * @param y
	 * @return 重叠子的纵坐标
	 */
	private  int getOverlapYOnTow(int[] arr,int y){
		if(arr[0]!=y){
			return arr[0];
		}else{
			return arr[1];
		}
	}

	/**
	 * 获取重叠子的纵坐标数组
	 * @param array
	 * @param chessNum
	 * @param x
	 * @param y
	 * @return 重叠子的纵坐标数组
	 */
	private  int[] getOverlapYArray(int[][] array,int chessNum ,int x){
		List<Integer> intList = new ArrayList<Integer>();
		for(int i=0;i<array.length;i++){
			if(chessNum == array[i][x]){
				intList.add(i);
			}
		}
		int len = intList.size();
		int Y[] = new int[len];
		for(int i=0;i<len;i++){
			Y[i]=intList.get(i);
		}
		return Y;
	}

	/**
	 * 根据颜色和前后两字的位置得出  前后
	 * @param color 颜色 ，R为红方
	 * @param Y 另一子
	 * @param y 被操作的子
	 * @return
	 */
	private  String judgePrefix(String color,int Y,int y){
		String prefix = "";
		if(color.equals(CHESS_COLOR_RED)){//红旗
			if(Y<y){//另一子在棋盘上y坐标要大
				prefix = REAR_CHESS;
			}else{
				prefix = FORMER_CHESS;
			}
		}else{//黑棋
			if(Y<y){//另一子在棋盘上y坐标要大
				prefix = FORMER_CHESS;
			}else{
				prefix = REAR_CHESS;
			}
		}
		return prefix;
	}

	/**
	 * 根据颜色判断前中后中的位置
	 * @param arr y的集合
	 * @param y 当前y
	 * @param color 子色
	 * @return prefix 前缀
	 */
	private  String judgePrefixByThree(int[] arr,int y,String color){
		int x = 0;
		String prefix = "";
		for(int i=0;i<arr.length;i++){
			if(arr[i] == y){
				x=i;
				break;
			}
		}
		if(color.equals(CHESS_COLOR_RED)){
			switch (x){
			case 0:
				prefix = FORMER_CHESS;
				break;
			case 1:
				prefix = CENTRE_CHESS;
				break;
			case 2:
				prefix = REAR_CHESS;
				break;
			}
		}else{
			switch (x){
			case 0:
				prefix = REAR_CHESS;
				break;
			case 1:
				prefix = CENTRE_CHESS;
				break;
			case 2:
				prefix = FORMER_CHESS;
				break;
			}
		}
		return prefix;
	}

	/**
	 * 一列跑出四五个子，判断此y在数组中的位置
	 * @param arr
	 * @param y
	 * @param color
	 * @param size 重叠子数目
	 * @return
	 */
	private  String judgePrefixByMore(int[] arr,int y,String color ,int size){
		int x = 0;
		String prefix = "";
		for(int i=0;i<arr.length;i++){
			if(arr[i] == y){
				x=i+1;
				break;
			}
		}
		if(color.equals(CHESS_COLOR_RED)){
			if(x==size){
				prefix = REAR_CHESS;
			}else if(x==1){
				prefix = FORMER_CHESS;
			}else{
				prefix = enCodeNum(x);
			}
		}else{
			if(x==size){
				prefix = FORMER_CHESS;
			}else if(x==1){
				prefix = REAR_CHESS;
			}else{
				prefix = enCodeNum(size-x+1);
			}
		}
		return prefix;
	}

	/**
	 * 根据颜色和两个位置得出动作，只受相对位移影响
	 * @param color	棋子颜色
	 * @param x 	原来x
	 * @param y		原来y
	 * @param i		后来x
	 * @param j		后来y
	 * @return
	 */
	private  String judgeAction(String color, int x, int y, int i, int j) {
		String action;
		if(color.equals(CHESS_COLOR_RED)){//红旗
			if(x==i&&y!=j){//走子后x不变,y变
				if(y<j){//走子后，y变大
					action = CHESS_ACTION_QUIT;
				}else{
					action = CHESS_ACTION_ADVANCE;
				}
			}else if(x!=i&&y==j){//x变，y不变
				action = CHESS_ACTION_HORIZONTAL;//平走为X左边
			}else{//两个全变  只有象士马
				if(y<j){
					action = CHESS_ACTION_QUIT;
				}else{
					action = CHESS_ACTION_ADVANCE;
				}
			}
		}else{//黑棋
			if(x==i&&y!=j){//走子后x不变,y变
				if(y>j){//走子后，y变小
					action = CHESS_ACTION_QUIT;
				}else{
					action = CHESS_ACTION_ADVANCE;
				}
			}else if(x!=i&&y==j){//y变，x不变
				action = CHESS_ACTION_HORIZONTAL;//平走为X左边
			}else{//两个全变  只有象士马
				if(y>j){
					action = CHESS_ACTION_QUIT;
				}else{
					action = CHESS_ACTION_ADVANCE;
				}
			}
		}
		return action;
	}

	/**
	 * 根据颜色判断位移叫法
	 * @param color	棋子颜色
	 * @param x 	原来x
	 * @param y		原来y
	 * @param i		后来x
	 * @param j		后来y
	 * @return
	 */
	private  String judgeDisplacement(String color, int x, int y, int i, int j) {
		String displacement = null;
		if(x==i){//走子后x不变，取位移，不受影响
			displacement = numberToString(absoluteValue(y-j));
		}else {//y不变，x变
			if(color.equals(CHESS_COLOR_RED)){//红旗
				displacement = enCodeNum(9-i);//平走为X左边，传入的是坐标系的，要用棋盘坐标系
			}else{
				displacement = numberToString(i+1);
			}
		}
		return displacement;
	}
	/**
	 * 返回绝对值
	 * @param num
	 * @return
	 */
	private  int absoluteValue(int num){
		return (int)(Math.sqrt((double)(num*num)));
	}
	
	/**
	 * 根据红旗还是黑棋返回棋子正负数
	 * @param color
	 * @return
	 */
	private int judgeDivisor(String color){
		if(color.equals(CHESS_COLOR_RED)){//红旗
			return 1;
		}else{
			return -1;
		}
	}
	void test(){
		System.out.println("==");
		if(true)
		return;
		System.out.println("--");
	}

}
