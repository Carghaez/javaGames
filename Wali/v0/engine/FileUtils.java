package javaGames.Wali.v0.engine;

import java.net.*;
import java.io.*;
import java.util.*;

class ExtensionFilter implements FilenameFilter
{
	private String extension;

	public ExtensionFilter( String extension )
	{
		this.extension = extension;
	}

	public boolean accept(File dir, String name)
	{
		return name.endsWith(extension);
	}
}

public class FileUtils
{
	// Dato un path, ritorna tutte le risorse
	public static List<URL> findURLs(final String name) throws IOException
	{
		return Collections.list(FileUtils.class.getClassLoader().getResources(name));
	}

	// Dato il nome di una risorsa, ritorna l'url associato
	public static URL getURL(final String name)
	{
		return FileUtils.class.getClassLoader().getResource(name);
	}

	// Dato un url ritorna solo il nome del file
	public static String getFileName(final URL url)
	{
		final String path = url.getPath();
		final String fileName = path.substring(path.lastIndexOf('/') + 1, path.length());
		//final String fileNameWithoutExtn = fileName.substring(0, fileName.lastIndexOf('.'));

		return fileName;
	}


	// Dato un path e un'estensione, ritorna l'elenco delle risorse
	public static String[] getFileNames(final String path, final String extension) throws IOException
	{
		String []ret = null;

	/*	try
		{
			final java.util.List<URL> urls = FileUtils.findURLs(path + "/*"); //*." + extension);
			for (URL url : urls)
				System.out.println(url.getPath());
		}
		catch (IOException e)
		{
		}*/

		// It is also possible to filter the list of returned files.
		File dir = new File(path);
		ret = dir.list(new ExtensionFilter(extension));

		// Complete the paths
		for (int i = 0; i < ret.length; i++)
			ret[i] = path + "/" + ret[i];

		/*
		final CodeSource src = main.class.getProtectionDomain().getCodeSource();
		final java.util.List<String> list = new ArrayList<String>();

		final URL jar = src.getLocation();
		final ZipInputStream zip = new ZipInputStream(jar.openStream());
		ZipEntry ze = null;

		while ((ze = zip.getNextEntry()) != null)
		{
			final String entryName = ze.getName();
			if (entryName.startsWith("XDnl/WaliGame/" + path) && entryName.endsWith(extension))
				list.add(entryName);
		}

		ret = new String[list.size()];
		list.toArray(ret);*/

		return ret;
	}


	// Legge i byte da un inputstream
	public static byte[] getBytes(final URL url) throws java.io.IOException
	{
		// Apro lo stream
		final InputStream is = url.openStream();

		// Get the size of the file
		long length = is.available();

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length	&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)
			offset += numRead;

		// Ensure all the bytes have been read in
		if (offset < bytes.length)
			throw new IOException("Could not completely read file ");

		// Close the input stream
		is.close();

		// Return bytes
		return bytes;
	}
}