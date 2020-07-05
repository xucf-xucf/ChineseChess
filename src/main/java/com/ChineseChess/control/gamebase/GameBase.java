package com.ChineseChess.control.gamebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.ChineseChess.model.OnMove;
import com.ChineseChess.model.User;
import com.common.SimulateCache;
import com.dbSource.base.NameSpace;
import com.dbSource.base.service.BaseService;
import com.webSocket.CCUtil;
import com.webSocket.CommonWebSocket;
import com.webSocket.WebSocketTest;

import net.sf.json.JSONArray;

import static com.common.ThreadLocalHelp.*;


@RequestMapping(value = "game")
@SessionAttributes({"user"})  
@Controller
public class GameBase extends BaseService{
	
	
	
	@RequestMapping(value = "requestGame", method = RequestMethod.POST ,produces="text/plain;charset=UTF-8")
	public  @ResponseBody String login(Model model) throws Exception{
		String status = SUCCESS_CODE;
		String desc = "";
		Map<String,String> paramMap = getParamMap();
		String rivalId = paramMap.get("rival");
		paramMap.put("requester",userT.get().getUserId());
		paramMap.put("responser",rivalId);
		paramMap.put("requesterName",userT.get().getUserName());
		paramMap.put("type",MSG_TYPE_REQUESTGAME);
		try{
			if(CommonWebSocket.getWebsocket(rivalId)!= null){//玩家在线则发送对战请求
				CommonWebSocket.getWebsocket(rivalId).sendMessage(resToJson(paramMap));
			}else{
				status = FAILED_CODE;
				desc = "玩家不在线！";
			}
		}catch (Exception e){
			status = FAILED_CODE;
			desc = "玩家已下线！";
		}
		paramMap.put("status", status);
		paramMap.put("desc", desc);
		return resToJson(paramMap);
	}
	@RequestMapping(value = "saveGame", method = RequestMethod.POST ,produces="text/plain;charset=UTF-8")
	public  @ResponseBody String saveGame(Model model) throws Exception{
		String status = SUCCESS_CODE;
		String desc = "";
		Map<String,Object> paramMap = getParamMap();
		Object mapA = paramMap.get("beginMap");
		JSONArray jsonArray = null;
		if(mapA == null){
			jsonArray = CCUtil.CHESS_INITIAL_JSONARRAY;
		}
		jsonArray = JSONArray.fromObject(mapA);//局面
		String gameMap = jsonArray.toString();
		String gameMark = (String)paramMap.get("gameMark");
		String userId = (String)paramMap.get("local");
		if(userId==null){
			userId = userT.get().getUserId();
		}
		String gameInfoT = (String)paramMap.get("CCEncode");
		String[] gameInfos = gameInfoT.split("\n");
		StringBuffer gameInfo = new StringBuffer();
		for(String s : gameInfos){
			gameInfo.append(s+"%%");
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("gameInfo", gameInfo.toString());
		map.put("gameType", "1");
		map.put("userR", userId);
		map.put("gameMark", gameMark);
		map.put("gameId", generateID());
		map.put("gameBoard", gameMap);
		try{
			baseDao.insert(NameSpace.BaseInfo, "saveGame",map);
		}catch(Exception e){
			status = FAILED_CODE;
			desc = e.getMessage();
		}
		paramMap.put("status", status);
		paramMap.put("desc", desc);
		return resToJson(paramMap);
	}
	@RequestMapping(value = "qryGame",produces = "text/plain;charset=UTF-8")
	public @ResponseBody String qryGame(){
		String status = SUCCESS_CODE;
		String desc = "success";
		Map<String ,Object >res=new HashMap<String,Object>();
		try {
			Map<String,Object> paramMap = getParamMap();
			String page = (String)(paramMap.get("page")==null?"1":paramMap.get("page"));
			int start = (Integer.parseInt(page)-1)*5;
			int pageSize = 5;
			paramMap.put("start", start);
			paramMap.put("pageSize", pageSize);
			paramMap.put("qryStr", paramMap.get("qryStr"));
			paramMap.put("userId",userT.get().getUserId());
			List<Map<String,String>>gameList = baseDao.selectList(NameSpace.BaseInfo,"qryGame", paramMap);
			res.put("gameList", gameList);
		} catch (Exception e) {
			status = FAILED_CODE;
			desc = e.getMessage();
			e.printStackTrace();
		}
		res.put("status", status);
		res.put("desc", desc);
		return resToJson(res);
	}
	@RequestMapping(value = "qryGameById",produces = "text/plain;charset=UTF-8")
	public @ResponseBody String qryGameById(){
		String status = SUCCESS_CODE;
		String desc = "success";
		Map<String ,Object >res=new HashMap<String,Object>();
		try {
			Map<String,String> paramMap = getParamMap();
			paramMap.put("gameId", paramMap.get("gameId"));
			Map<String,String> game = baseDao.selectOne(NameSpace.BaseInfo,"qryGameById", paramMap);
			String gameBoard = game.get("game_board");
			JSONArray gameMap = new JSONArray();
			if(gameBoard==null ||gameBoard.length()<10){
				gameMap = CCUtil.CHESS_INITIAL_JSONARRAY;
			}else{
				gameMap = JSONArray.fromObject(gameBoard);
			}
			res.put("gameMap", gameMap);
			String gameInfo = game.get("game_info");
			String[] gameInfos = gameInfo.split("%%");
			res.put("CCEncode",gameInfos);
			String color = "";
			CCUtil cUtil = new CCUtil();
			JSONArray gameMapTemp = gameMap;
			List<OnMove> gameList = new ArrayList<OnMove>();
			for(int i=0;i<gameInfos.length;i++){
				if(i%2==0){
					color = "R";
				}else{
					color = "B";
				}
				OnMove om = cUtil.deCodeCC(gameInfos[i], color, gameMapTemp);
				gameMapTemp = om.getMap();
				gameList.add(om);
			}
			res.put("gameList", gameList);
		} catch (Exception e) {
			status = FAILED_CODE;
			desc = e.getMessage();
			e.printStackTrace();
		}
		res.put("status", status);
		res.put("desc", desc);
		return resToJson(res);
	}
	/**
	 * 统计对局数
	 * @return
	 */
	@RequestMapping(value = "countGame",produces = "text/plain;charset=UTF-8")
	public @ResponseBody String countGame(){
		String status = SUCCESS_CODE;
		String desc = "success";
		Map<String ,Object >res=new HashMap<String,Object>();
		try {
			Map<String,Object> paramMap = getParamMap();
			paramMap.put("qryStr", paramMap.get("qryStr"));
			paramMap.put("userId",userT.get().getUserId());
			String count = baseDao.selectOne(NameSpace.BaseInfo,"qryGameCount", paramMap);
			res.put("count", count);
		} catch (Exception e) {
			status = FAILED_CODE;
			desc = e.getMessage();
			e.printStackTrace();
		}
		res.put("status", status);
		res.put("desc", desc);
		return resToJson(res);
	}
	/**
	 * 对局结束，保存棋局
	 * @param arrayList
	 * @throws Exception 
	 */
	@RequestMapping(value = "savePlayerGame",produces = "text/plain;charset=UTF-8")
	public @ResponseBody String saveGame() throws Exception{
		String status = SUCCESS_CODE;
		String desc = "success";
		Map<String ,Object >res=new HashMap<String,Object>();
		Map<String,Object> paramMap = getParamMap();
		String winUser = (String) paramMap.get("local");
		String winUserName = (String) paramMap.get("localName");
		String rival = (String) paramMap.get("rival");
		String rivalName = (String) paramMap.get("rivalName");
		String key = WebSocketTest.judgeMapMixtureKey(winUser,rival);
		ArrayList<String> gameList = SimulateCache.get(key);
		StringBuilder stringBuilder = new StringBuilder();
		for(String str : gameList){
			//定义数据库中存储的数据结构，
			str = str+CHESSINFO_SPLIT_STR;
			stringBuilder.append(str);
		}
		//存到数据库
		Map<String,String>map = new HashMap<String,String>();
		map.put("gameId", generateID());
		map.put("gameType", "0");
		map.put("gameInfo",stringBuilder.toString());
		String[] keys = key.split("_");
		map.put("userR", keys[0]);
		map.put("userB", keys[1]);
		int len = gameList.size()/2;
		map.put("gameMark", winUserName+len+"手胜"+rivalName);
		if(paramMap.get("peace")!=null){
			map.put("gameMark", winUserName+len+"手平"+rivalName);
		}else{
			map.put("winUser",winUser);
		}
		map.put("roundCount", len+"手");
		map.put("gameBoard",CCUtil.CHESS_INITIAL_MAP);
		baseDao.insert(NameSpace.BaseInfo, "saveGame",map);
		//存储完毕。模拟缓存中清除
		SimulateCache.remove(key);
		Map<String,String> m = new HashMap<String,String>();
		m.put("type", MSG_TYPE_WIN);
		CommonWebSocket.getWebsocket(winUser).sendMessage(resToJson(m));
		CommonWebSocket.getWebsocket(rival).sendMessage(resToJson(m));
		res.put("status", status);
		res.put("desc", desc);
		return resToJson(res);
	}
	
}
