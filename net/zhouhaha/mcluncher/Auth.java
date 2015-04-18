package net.zhouhaha.mcluncher;

import java.io.*;
import java.net.*;

public class Auth {
    public Auth(){
    }
    public String AuthRequest(String username,String password) throws MalformedURLException, IOException, AuthException{
        URL url = new URL("https://authserver.mojang.com/authenticate");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setUseCaches(false);
        conn.setInstanceFollowRedirects(true);
        conn.setConnectTimeout(20000); 
        conn.setReadTimeout(300000);
        conn.setRequestProperty("Content-Type","application/json");
        conn.connect();
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        String json = (("{\n" +
            "\"agent\": {\n" +
            "\"name\": \"Minecraft\",\n" +
            "\"version\": 1\n" +
            "},\n" +
            "\"username\": \"%=USERNAME=%\",\n" +
            "\"password\": \"%=PASSWORD=%\"\n" +
            "}").replace("%=USERNAME=%", username).replace("%=PASSWORD=%", password));
        out.writeBytes(json);
        if(conn.getResponseCode()==403){
            throw new AuthException("登录失败!");
        }
        DataInputStream in = new DataInputStream(conn.getInputStream());
        return in.readLine();
    }
}
