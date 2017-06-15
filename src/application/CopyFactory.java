package application;

import java.io.File;

public class CopyFactory  {
	private static File directory;
	private static File fileToCopy;
	
	@SuppressWarnings("unused")
	public CopyFactory(File dir, File file) {
		directory = dir;
		fileToCopy = file;
		MediaCopy mc = getType(fileToCopy);
		
	}

	public static MediaCopy getType(File file){
		
		String fileName = file.getName();
		String extension = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
		if(file != null){
			if(extension.equalsIgnoreCase("mp3")){
				return new MusicCopy(file,directory);
			}
			if(extension.equalsIgnoreCase("jpg")){
				return new PictureCopy(file,directory);
			}
		}
		
		return null;
	}

	

	
}
