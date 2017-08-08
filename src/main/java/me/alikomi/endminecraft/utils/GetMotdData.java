package me.alikomi.endminecraft.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class GetMotdData {

    private String jsondata;
    private JSONObject main;
    private JSONObject version;
    private JSONObject players;

    public GetMotdData(String returnjson) {
        this.jsondata = returnjson;
        main = JSON.parseObject(jsondata);
        version = main.getJSONObject("version");
        players = main.getJSONObject("players");
    }
    public String getVersion() {
        return version.getString("name");
    }
    public int getOnlinePlayers() {
        return players.getInteger("online");
    }
    public int getMaxPlayers() {
        return players.getInteger("max");
    }



}
