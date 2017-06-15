package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class PictureCopy implements MediaCopy {

	private File directory;
	private File fileToCopy;
	
	public PictureCopy(File file, File dir) {
		directory = dir;
		fileToCopy = file;
		copyFile(fileToCopy);
	}

	public void copyFile(File file) {
		InputStream input = null;
		OutputStream output = null;
		try {
			input = new FileInputStream(file);
			output = new FileOutputStream(directory);
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = input.read(buf)) > 0) {
				output.write(buf, 0, bytesRead);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			try{
				input.close();
				output.close();
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
	}

}
