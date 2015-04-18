package net.zhouhaha.mcluncher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.json.JSONException;

public class GenLuncher {
    private UUID uid;
    private List<String> ifiles = new ArrayList<String>();
    private String cmd = new String();
    private String args = new String();
    public GenLuncher(){
        uid = UUID.randomUUID();
    }
    public String Gen(String java,String mcp,String username,String version,String assetspath,String gamedir,int xmx) throws IOException, JSONException, AuthException{
        return this.Gen(java, mcp, username, version, assetspath, gamedir, xmx, false,"");
    }
    public String Gen(String java,String mcp,String username,String version,String assetspath,String gamedir,int xmx,boolean login,String password) throws IOException, JSONException, AuthException{
        File mcpf = this.getMinecraftFolder(new File(mcp));
        File j = new File(mcpf + "\\versions\\" + version + "\\" + version + ".json");
        File libf = new File(mcpf.getAbsoluteFile() + "\\libraries\\");
        Json json = new Json();
        ifiles = json.getLibs(j,libf);
        StringBuilder slib = new StringBuilder();
        for(Object obj:ifiles){
            slib.append(obj).append(";");
        }
        slib.append(mcpf.getAbsolutePath()).append("\\versions\\").append(version).append("\\").append(version).append(".jar");
        cmd = this.getBasePath();
        this.replace("java",java);
        this.replace("xmx",xmx);
        this.replace("lib", slib.toString());
        this.replace("mainclass", json.getMainClass(j));
        this.args = json.getArgs(j);
        arg("${auth_player_name}",username);
        arg("${version_name}",version);
        arg("${game_directory}",gamedir);
        arg("${game_assets}",assetspath);
        if(login){
            Auth auth = new Auth();
            String tjson = auth.AuthRequest(username, password);
            arg("${auth_access_token}",json.getToken(tjson));
            arg("${auth_uuid}",json.getToken(tjson));
            args+=" --userType Mojang";
        }
        this.replace("arg", this.args);
        return cmd;
    }
    private File getMinecraftFolder(File f){
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("win")){
            return new File(f,".minecraft/");
        }else if(os.contains("mac")){
            return new File(f,"Library/Application Support/minecraft");
        }else if(os.contains("linux") || os.contains("unix")){
            return new File(f,".minecraft/");
        }else{
            return new File(f,"minecraft/");
        }
    }
    private void replace(String var,Object to){
        this.cmd = this.cmd.replace("%" + var.toUpperCase() + "%",to.toString());
    }
    private void arg(String var,Object to){
        this.args = this.args.replace(var, to.toString());
    }
    private String getBasePath(){
        return new String("\"%JAVA%\" " +
            "-Xmx%XMX%m " +
            "-Dfml.ignoreInvalidMinecraftCertificates=true " +
            "-Dfml.ignorePatchDiscrepancies=true " +
            "-Djava.library.path=\".minecraft\\natives\" " +
            "-cp \"%LIB%\" " +
            "%MAINCLASS% " +
            "%ARG%");
    }
}
