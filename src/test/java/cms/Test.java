package cms;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;

public class Test {

	public static void main(String[] args) throws ParseException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
		// TODO Auto-generated method stub

//		String a=",faf,fafaf,fff";
//		System.out.println(a.substring(0,a.lastIndexOf(",")));
		
		
//		 Pattern ipPattern = Pattern.compile("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");
//	     Matcher matcher = ipPattern.matcher("192.168.1.1");
//	     if (matcher.find()) {
//	    	 System.out.println("ok");
//	     }
		
//		System.out.println(Charsets.UTF_8.name());
		
		//System.out.println("根据手机端引导页引导页的目的、出发点不同，可以将其分为功能介绍类、使用说明类、推广类、问题解决类，一般引导页不会超过5页。".length());
//		Date date = DateUtils.parseDate("2017-05-22 22:22:22", new String[]{"yyyy-MM-dd","yyyy-MM-dd HH:mm:ss"});
//		Date newDate = DateUtils.addDays(date, 158);
//		System.out.println(DateFormatUtils.format(newDate, "yyyy-MM-dd HH:mm:ss"));
//		System.out.println("fafaf.fafaf.fafaf".replace(".", "/"));
//		System.out.println(BeanUtils.describe(new Admin()));
	    
//	    System.out.println(HttpKit.get("https://www.saiwuquan.com/pc"));
//	    FilenameUtils.getName(filename)
	    
//	    FileUtils.copyURLToFile()
//	    String a = Engine.use().getTemplateByString("fafefef#(a)").renderToString(Kv.by("a", "123"));
//	    System.out.println(a);
	    
//	    System.out.println(new Date(1525934808000l));
//	    Db.query(sql);
	    
//	    String str = "varchar(225)";
//	    Pattern pattern = Pattern.compile("(?<=\\()[^\\)]+");
//	    Matcher matcher = pattern.matcher(str);
//	    while(matcher.find())
//	    {
//	    System.out.println(matcher.group());
//	    }
	    
//	    URLClassLoader urlClassLoader = (URLClassLoader)  Test.class.getClassLoader();
//        URL[] urLs = urlClassLoader.getURLs();
//        for (URL url : urLs) {
//            String path = url.getPath();
//            if(path.endsWith(".jar")){
//                if (path.startsWith("/") && path.indexOf(":") == 2) {
//                    path = path.substring(1);
//                }
//                JarFile jarFile = new JarFile(path);
//                Attributes mainAttributes = jarFile.getManifest().getMainAttributes();
//                String exportPackage = mainAttributes.getValue("Export-Package");
//                System.out.println(exportPackage);
//                
//            }
//        }
	    
//	    System.out.println(StringEscapeUtils.escapeJava("\r\n"));
	    
//	    System.out.println(Long.toString(System.currentTimeMillis() / 1000));
		
//		String[] urls = "/a/111".split("/");
//		System.out.println(urls.length);
//		System.out.println("/".split("/").length);
		
//		String fileContent = FileUtils.readFileToString(new File("c:\\b2c.sql"));
//		//http://image.demo.b2b2c.shopxx.net/6.0/
////		String newfileContent = fileContent.replace("http://image.demo.b2b2c.shopxx.net/6.0/", "/upload/image/");
////		FileUtils.write(new File("c:\\myb2b2c.sql"),newfileContent);
//		String newfileContent = fileContent.replace("`version` bigint(20) NOT NULL,", "");
//		FileUtils.write(new File("c:\\b2c.sql"),newfileContent);
		
//		String a = Jsoup.parse("&lt;/span&gt;&lt;/p&gt;").text();
	}

}
