package test;

import iqq.comm.Auth;
import iqq.model.Category;
import iqq.model.Member;
import iqq.service.CategoryService;
import iqq.service.HttpService;
import iqq.service.LoginService;
import iqq.service.MemberService;
import iqq.ui.LoginProcessPanel;
import iqq.util.ErrorMessage;
import iqq.util.Log;
import iqq.util.Method;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

class StreamToString{
	public static String ConvertToString(InputStream inputStream){
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		StringBuilder result = new StringBuilder();
		String line = null;
		try {
			while((line = bufferedReader.readLine()) != null){
				result.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try{
				inputStreamReader.close();
				inputStream.close();
				bufferedReader.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		return result.toString();
	}


	public static String ConvertToString(FileInputStream inputStream){
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		StringBuilder result = new StringBuilder();
		String line = null;
		try {
			while((line = bufferedReader.readLine()) != null){
				result.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try{
				inputStreamReader.close();
				inputStream.close();
				bufferedReader.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		return result.toString();
	}
	
}

public class TestUrl1 {
	
	public String get(String httpUrl){
		try{
			URL url = new URL(httpUrl);
		    System.getProperties().put("proxySet","true");  
		    System.getProperties().put("proxyHost","192.168.174.169");  
		    System.getProperties().put("proxyPort","3128");  
			HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
			//GET Request Define: 
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();
			
			//Connection Response From Test Servlet
			InputStream inputStream = urlConnection.getInputStream();
			
			//Convert Stream to String
			String responseStr = StreamToString.ConvertToString(inputStream);
			return responseStr;
		}catch(IOException e){
			Logger.getLogger(TestUrl1.class.getName()).log(Level.SEVERE, null, e);
			return e.getMessage();
		}
	}
	
	public String verifycode(){
		TestUrl1 demo=new TestUrl1();
		String httpUrl = "http://check.ptlogin2.qq.com/check?uin=137742685&appid=1003903&r=0.6331230279734363";
		HttpService httpService = new HttpService(httpUrl, Method.GET);
		String result = httpService.sendHttpMessage();
		//String result = demo.get(httpUrl);
		return result;
	}
	
	 // 加密密码
    public String encodePass(String password, String verifyCode, String verifyCodeHex) {
        try {
            InputStream in = this.getClass().getResourceAsStream("/iqq/res/encodePass.js");
            Reader inreader = new InputStreamReader(in);
            ScriptEngineManager m = new ScriptEngineManager();
            ScriptEngine se = m.getEngineByName("javascript");
            se.eval(inreader);
            Object t = se.eval("passwordEncoding(\"" + password + "\",\"" + verifyCodeHex
                    + "\",\"" + verifyCode.toUpperCase() + "\");");

            return t.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Icon getCaptcha(String vcType) {
        String url = "http://captcha.qq.com/getimage?aid=1003903&r=" + Math.random() + "&uin=137742685";
        HttpService hs = new HttpService(url, Method.GET);
        Image image = null;
        try {
            image = ImageIO.read(hs.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(LoginService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ImageIcon(image);
    }
	
	public void login(){
		TestUrl1 demo=new TestUrl1();
		String result = demo.verifycode();
		System.out.println(result);
        // 检查账号验证码返回结果
        String checkAccountVCRegex = "\\'(.+)\\'\\,\\'(.+)\\'\\,\\'(.+)\\'";
        Pattern p = Pattern.compile(checkAccountVCRegex);
        Matcher m = p.matcher(result);
        String checkType = "";
        String vCode = "";
        String enVerifyCode = "";
        String verifycodeHex = "";
        if (m.find()) {
            checkType = m.group(1);
            enVerifyCode = m.group(2);
            verifycodeHex = m.group(3);
            System.out.println("CheckType:" + checkType + ",enVerifyCode:" + enVerifyCode + ",VerifycodeHex:" + verifycodeHex + ",groupCount:" + m.groupCount());
        }
        
        String loginUrl = "http://ptlogin2.qq.com/login?u=137742685&p=:password&verifycode=:VCode&webqq_type=10&remember_uin=1&login2qq=1&aid=1003903&u1=:loginurl&h=1&ptredirect=0&ptlang=2052&from_ui=1&pttype=1&dumy=&fp=loginerroralert&action=7-24-1937704&mibao_css=m_webqq&t=1&g=1";
        if (!enVerifyCode.startsWith("!")) {
            // 生成图片验证码
            Log.println("enVerifyCode:" + enVerifyCode);
            //enVerifyCode = JOptionPane.showInputDialog(null, "验证码：", "请输入验证码：");
            Icon icon = getCaptcha(enVerifyCode);
            if (icon != null) {
                vCode = (String) JOptionPane.showInputDialog(null, "验证码：", "请输入验证码：", JOptionPane.QUESTION_MESSAGE, icon, null, null);
                if (vCode == null || vCode.trim().equals("")) {
                    ErrorMessage.show("验证码输入有误!");
                }
            } else {
                ErrorMessage.show("验证码获取失败！请重试。");
            }

        } else {
            vCode = enVerifyCode;
        }
        System.out.println(vCode);
		String password=encodePass("kill7025799", vCode.toUpperCase(), verifycodeHex);
		String u1="http%3A%2F%2Fweb3.qq.com%2Floginproxy.html%3Flogin2qq%3D1%26webqq_type%3D10";
		System.out.println(password);
		String httpUrl = "http://ptlogin2.qq.com/login?u=137742685&p="+password+"&verifycode="+vCode+"&webqq_type=10&remember_uin=1&login2qq=1&aid=1003903&u1="+u1+"&h=1&ptredirect=0&ptlang=2052&from_ui=1&pttype=1&dumy=&fp=loginerroralert&action=7-24-1937704&mibao_css=m_webqq&t=1&g=1";
		loginUrl = loginUrl.replace(":password", password);
	    loginUrl = loginUrl.replace(":VCode", vCode.toUpperCase());
	    String loginStr = "http%3A%2F%2Fweb3.qq.com%2Floginproxy.html%3Flogin2qq%3D1%26webqq_type%3D10";
	    //String loginStr = "http://web3.qq.com/loginproxy.html?login2qq=1&webqq_type=10";
	    loginUrl = loginUrl.replace(":loginurl", loginStr);
		
	    HttpService httpService = new HttpService(loginUrl, Method.GET);
        result = httpService.sendHttpMessage();
		//String result1 = demo.get(loginUrl);
		//System.out.println(result1);
	}
	
	public static void main(String[] args) {
		TestUrl1 demo=new TestUrl1();
		//demo.login();
	    LoginService loginService = LoginService.newInstance();
	    MemberService memberService = MemberService.getInstance();
	    CategoryService categoryService = CategoryService.getInstance();
		Map<String,String> loginParam=new HashMap<String,String>();
		loginParam.put("account", "137742685");
		loginParam.put("password", "kill7025799");
		loginParam.put("verifyCode", "adcdefg");
		loginParam.put("loginStatus", "登录你猜猜看成功了吗？");
		boolean isTrue = loginService.login(loginParam);
		if(!isTrue){
			System.out.println(isTrue+"|登录失败！");
			return ;
		}
		
		try {
			System.out.println(Auth.getMember());
			Member member = memberService.getMemberInfo(Auth.getMember());
            member.setFace(memberService.getFace());
            List<Category>  categoryList = categoryService.getFriends();
            List<Member> onlineFriends = categoryService.getOnlineFriends();
            System.out.println(member);
            System.out.println(categoryList);
            System.out.println(onlineFriends);
        } catch (Exception ex) {
            Logger.getLogger(LoginProcessPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
}
