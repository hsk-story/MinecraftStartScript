package net.zhouhaha.mcluncher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Json {
    public UUID uid;
    public Json(){
        uid = UUID.randomUUID();
    }
    public String getMainClass(File json) throws IOException, JSONException{
        JSONObject j = new JSONObject(this.getFileText(json));
        return j.getString("mainClass");
    }
    public String getArgs(File json) throws IOException, JSONException{
        JSONObject j = new JSONObject(this.getFileText(json));
        return j.getString("minecraftArguments");
    }
    public List<String> getLibs(File json,File libfolder) throws IOException, JSONException{
        JSONObject j = new JSONObject(this.getFileText(json));
        JSONArray ja = j.getJSONArray("libraries");
        List<String> libs = new ArrayList<String>();
        for(int i=0;i < ja.length();i++){
            String[] str = new JSONObject(ja.getString(i)).getString("name").split(":");
            File jarpath = new File(libfolder.getAbsolutePath()+"\\"+str[0].replace('.', '\\') + "\\" + str[1] + "\\" + str[2] + "\\");
            File jarfile = getFolderJar(jarpath);
            if(jarfile!=null)
            libs.add(jarfile.getAbsolutePath());
        }
        return libs;
    }
    public String getToken(String json) throws JSONException{
        JSONObject j = new JSONObject(json);
        return j.getString("accessToken");
    }
    public File getFolderJar(File f){
        if(!f.exists()) return null;
        return f.listFiles()[0];
    }
    private String getFileText(File json) throws FileNotFoundException, IOException{
        FileInputStream fr = new FileInputStream(json);
        byte buffer[] = new byte[(int)json.length()]; 
        fr.read(buffer);
        return new String(buffer);
    }
}
