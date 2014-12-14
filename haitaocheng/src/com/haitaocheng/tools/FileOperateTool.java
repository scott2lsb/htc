/**
 * 
 */
package com.haitaocheng.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 类描述:
 * @author pzp  2011-12-12
 */
public class FileOperateTool {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Properties properties = System.getProperties();
		String srcFileName = (String)properties.get("user.dir") + "/docs/TestConsole.java"; 
		
		
/*		
 		// 读取文本文件的所有内容
		String content = getStringContentByText(srcFileName, "UTF-8");
		System.out.println(content);
*/		
		
		/*
		// 使用字符流，读取一个文件，保存为另外一个文件
		String destFileName = (String)properties.get("user.dir") + "/docs/temp.java"; 
		saveFileByFileReaderAndWriter(srcFileName, destFileName);
		*/
		
		/*
		// 使用字节流，读取一张图片，保存为另外一张图片
		srcFileName = (String)properties.get("user.dir") + "/docs/java io2.gif"; 
		String destFileName = (String)properties.get("user.dir") + "/docs/temp.gif"; 
		saveFileByFileInputAndOutputStream(srcFileName, destFileName);
		*/
		
		srcFileName = (String)properties.get("user.dir") + "/docs/temp/temp2/devide.java"; 
		String[] minFileNames = divide(srcFileName, 1024);	// 分割文件
		for (String minFileName : minFileNames) {
			System.out.println(minFileName);
		}
		
		String destFileName = (String)properties.get("user.dir") + "/docs/temp/temp2/unit.java"; 
		unit(minFileNames, destFileName);	// 合并文件
	}
	

	/**
	 * 读取文本文件的所有内容
	 * @param fileName
	 * @param encoding
	 * @return
	 */
	public static String getStringContentByText(String fileName, String encoding) {
		StringBuilder builder = new StringBuilder();
//		File file = new File(fileName);	// 文件对象
//		FileReader fileReader = null;	// 文件字符输入流
		BufferedReader bufferedReader = null;	// 文件字符缓冲流
		try {
			// bufferedReader = new BufferedReader(new FileReader(new File(fileName)));
			bufferedReader = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(
									new File(fileName)),encoding));
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {	// 每次读取一行
				builder.append(line);
				builder.append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(bufferedReader);
		}
		return builder.toString();
	}
	

	/**
	 * 使用字符流保存文件
	 * @param srcFileName
	 * @param destFileName
	 */
	public static void saveFileByFileReaderAndWriter(String srcFileName, String destFileName) {
		BufferedReader bufferedReader = null;	// 文件字符缓冲流
		BufferedWriter bufferedWriter = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(new File(srcFileName)));
			bufferedWriter = new BufferedWriter(new FileWriter(new File(destFileName)));
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {	// 每次读取一行
				bufferedWriter.write(line);	// 写文 件  一行
				bufferedWriter.newLine();
				bufferedWriter.flush();
			}
			System.out.println("文件保存成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("文件保存失败");
		} finally {
			close(bufferedWriter,bufferedReader);
		}
	}
	
	
	//===========================================================

	/**
	 * 使用字节流保存文件
	 * @param srcFileName
	 * @param destFileName
	 */
	public static void saveFileByFileInputAndOutputStream(String srcFileName, String destFileName) {
		BufferedInputStream input = null;			// 字节输入缓冲流
		BufferedOutputStream output = null;		// 字节输出缓冲流
		try {
			// 使用文件字节输入流构造  输入缓冲流
			input = new BufferedInputStream(new FileInputStream(new File(srcFileName)));
			// 使用文件字节输出流构造  输出缓冲流
			output = new BufferedOutputStream(new FileOutputStream(destFileName));
			byte[] buffer = new byte[1024]; // 定义每次读取的字节
			int count = 0;
			while ( (count = input.read(buffer)) != -1){ 		// 读取,当为-1时。表示已经读完
				 output.write(buffer, 0,  count);				// 写入
				 output.flush();	//刷新
			}
			System.out.println("文件保存成功！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("文件保存失败！");
		}  finally {
			close(output, input);
		}
	}
	
	/**
	 * 使用字节流保存文件
	 * @param srcFileName
	 * @param destFileName
	 */
	public static void saveFileByFileInputAndOutputStream(InputStream inputStream, String destFileName) {
		BufferedInputStream input = null;			// 字节输入缓冲流
		BufferedOutputStream output = null;		// 字节输出缓冲流
		
		// 如果文件的父文件夹不存在，则创建一个
		File file = new File(destFileName);
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		} 
		// 如果文件存在，就删除
		else if (file.exists()) {
			file.delete();
		}
		
		try {
			// 使用文件字节输入流构造  输入缓冲流
			input = new BufferedInputStream(inputStream);
			// 使用文件字节输出流构造  输出缓冲流
			output = new BufferedOutputStream(new FileOutputStream(destFileName));
			byte[] buffer = new byte[1024]; // 定义每次读取的字节
			int count = 0;
			while ( (count = input.read(buffer)) != -1){ 		// 读取,当为-1时。表示已经读完
				 output.write(buffer, 0,  count);				// 写入
				 output.flush();	//刷新
			}
			System.out.println("文件保存成功！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("文件保存失败！");
		}  finally {
			close(output, input);
		}
	}

	/**
	 * 把一个文件分割成多个小文件
	 * @param srcFileName	待分割的文件名
	 * @param size			小文件的大小
	 * @return				分割成的小文件的文件名数组
	 * @throws Exception 
	 */
	public static String[] divide(String srcFileName, int size) throws Exception {
		File file = new File(srcFileName);
		// 文件不存在，或者不是文件
		if (!file.exists() || !file.isFile()) {
			return new String[0];
		}
		
		long fileLength = file.length();	// 获得文件的大小
		int num = 0;						// 分割后的小文件的数目
		if (fileLength % size == 0) {
			num = (int) (fileLength / size);
		} else {
			num = (int) (fileLength / size) + 1;
		}
		
		// 定义数组返回值，用于保存分割成的小文件的文件名
		String[] outFileNames = new String[num];
		
		FileInputStream input = null; 	// 定义输入流
		File outFileName = null;		// 小文件
		FileOutputStream output = null;	// 定义输出流
		try {
			input = new FileInputStream(file);
			byte[] buffer = new byte[size];	// 每个小文件读的字节
			
			for (int i = 0; i < num; i++) {	// 循环生成num个小文件
				outFileName = new File(file.getAbsolutePath() + ".min" + (i+1));	// 小文件
				output = new FileOutputStream(outFileName);	//小文件输出流
				int count = input.read(buffer, 0 ,size);	// 读
				output.write(buffer, 0, count);			// 写
				
				FileOperateTool.close(output); // 关闭流
				outFileNames[i] = outFileName.getAbsolutePath();	// 保存小文件名
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			FileOperateTool.close(input);
		}
		return outFileNames;
	}


	
	/**
	 * 把多个小文件合并成一个大文件
	 * @param minFileNames
	 * @param destFileName
	 * @return 是否合并成功
	 */
	public static boolean unit(String[] minFileNames, String destFileName) {
		FileOutputStream output = null;
		FileInputStream input = null;
		try {
			output = new FileOutputStream(destFileName);	// 创建合并文件的输出流
			for (int i = 0; i < minFileNames.length; i++) {
				input = new FileInputStream(minFileNames[i]);	// 创建小文件的输入流
				int count = 0;
				while((count = input.read()) != -1) {
					output.write(count);
				}
				FileOperateTool.close(input);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("合并失败！");
			return false;
		} finally {
			FileOperateTool.close(output);
		}
		System.out.println("合并成功！");
		return true;
	}

	
	/**
	 * @param <T>		T 代表泛型，T extends Closeable 表示 T必须是满足条件，是Closeable的子类
	 * @param ts		T... ts 代表 不定参数
	 */
	public static<T extends Closeable>  void close(T... ts) {
		for (T t : ts) {
			if (null == t) {
				continue;
			}
			try {
				t.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return;
	}


}


// 文件复制工具类
// 写一个方法： 复制 “学习集合框架”这个目录下所有的.java文件



