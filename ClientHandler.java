import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

/**
 * 线程类，用于处理客户端请求和响
 *
 */

public class ClientHandler implements Runnable {
	private Socket socket;
	
	public ClientHandler(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {		// 重写run方法，响应代码提取
		try {
			/*
			 * Http协议响应格式
			 * Http/1.1 200 OK
			 * Content-type:test/html
			 * Content-Length:文件的数据长度
			 * 空白行
			 * 响应实体
			 */
			HttpRequest request = new HttpRequest(socket.getInputStream());		// HTTPRequest对象
			HttpResponse response = new HttpResponse(socket.getOutputStream());	// HttpRespose对象
			File file=null;
			String path = "D:/lab8/";
			String STUDENT_ID = "3170105896";
			String STUDENT_PASS = "5896";
			if(request.getMethod().equals("POST")){
				if(request.getUri().equals("/dopost")){
					response.setStatus(200);
					Map<String, String> dict = request.getDict();
					if(STUDENT_ID.equals(dict.get("login").toString()) && STUDENT_PASS.equals(dict.get("pass").toString())){
						System.out.println(1);
						file = new File(path + "success.txt");        // 获取文件目录
					}
					else{
						file = new File(path + "fail.txt");        // 获取文件目录
					}
				}
				else {
					response.setStatus(404);
					file = new File(path + "404.txt");        // 获取文件目录
				}
			}
			else {
				if (getContentTypeFileExt(request.getUri()).contentEquals("jpg")) {
					file = new File(path + "img" + request.getUri());        // 获取文件目录
				} else if (getContentTypeFileExt(request.getUri()).contentEquals("txt")) {
					file = new File(path + "txt" + request.getUri());
				} else {
					file = new File(path + "html" + request.getUri());
				}
				if (!file.exists()) {        // 访问页面不存在
					file = new File(path + "404.txt");
					response.setStatus(404);            // 修改状态码为404，没有资源
				} else {
					response.setStatus(200);        // 状态码
				}
			}
			response.setProtocol(request.getProtocol());
			response.setContentTyep(getContentTypeFileExt(file));    // 文件类型
			response.setContentLength((int) file.length());        // 文件长度
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));		// 读取网页文件并输出
			byte[] bs = new byte[(int)file.length()];
			bis.read(bs);		// 把bis读入数组bs中
			response.getOutputStream().write(bs);		// 输出数组bs中的内容
			response.getOutputStream().flush();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getContentTypeFileExt(File file) {
		String fileName = file.getName();		// 获取访问文件名
		String type = fileName.substring(fileName.lastIndexOf(".") + 1);		// 获取后缀(html)
		return type;
	}
	private String getContentTypeFileExt(String fileName) {
		String type = fileName.substring(fileName.lastIndexOf(".") + 1);		// 获取后缀(html)
		return type;
	}
}

