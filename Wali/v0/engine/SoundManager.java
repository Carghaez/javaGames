package javaGames.Wali.v0.engine;

import java.util.Vector;
import java.util.HashMap;
import java.io.*;
import javazoom.jl.player.Player;

// Stato di un'istanza del suono
enum SoundState { STOP, PLAY, PAUSE };

// Suono
class Sound
{
	// Compressed data
	private byte []data;

	// Istanze del suono
	private Vector<SoundInstance> instances;
	public static byte[] getBytesFromInputStream(InputStream is) throws IOException
	{
		// Get the size of the file
		long length = is.available();

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
		&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
		offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
		throw new IOException("Could not completely read file ");
		}

		// Close the input stream and return bytes
		is.close();
		return bytes;
	}

	// Carica un suono da file
	public Sound(final String fileName)
	{
		instances = new Vector<SoundInstance>();

		try
		{
			/*RandomAccessFile f = new RandomAccessFile(fileName, "r");
			data = new byte[(int)f.length()];
			f.read(data);*/

			// Hardcoded for now :(
			data = getBytesFromInputStream(Sound.class.getClassLoader().getResourceAsStream(fileName));
		}
		catch (java.io.IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}

	// Crea un'istanza
	public SoundInstance instance()
	{
		final SoundInstance newInstance = new SoundInstance(this);
		instances.add(newInstance);

		return newInstance;
	}

	// Cleanup method
	public void cleanup()
	{
		for (SoundInstance i : instances)
			i.cleanup();

		instances.clear();
	}

	// Get methods
	public byte[] getData() { return data; }
	public Vector<SoundInstance> getInstances() { return instances; }
}

// Istanza di un suono
class SoundInstance
{
	// Riferimento al suono
	private Sound soundRef;

	// Stato del suono
	private SoundState state;

	// Thread associato
	private Thread playerThread;
	private Player player;

	public SoundInstance(final Sound soundRef)
	{
		this.soundRef = soundRef;
		state = SoundState.STOP;
	}

	public void stop()
	{
		if (state == SoundState.PLAY)
		{
			// Interrompo il thread
			playerThread.interrupt();
			playerThread = null;

			// Chiudo il player
			player.close();
			player = null;

			// Cambio stato
			state = SoundState.STOP;
		}
	}

	public void play()
	{
		if (state == SoundState.STOP)
		{
			// Creo il player
			try
			{
				player = new Player(new BufferedInputStream(new ByteArrayInputStream(soundRef.getData())));

				// Create the new thread
				playerThread =  new Thread()
							{
								public void run()
								{
									try
									{
										player.play();
									}
									catch (Exception e)
									{
										System.out.println(e);
									}
								}
							};

				// Start playing
				playerThread.start();
				state = SoundState.PLAY;
			}
			catch (javazoom.jl.decoder.JavaLayerException jle)
			{
				jle.printStackTrace();
			}
		}
	}

	// Cleanup method
	public void cleanup()
	{
		stop();
	}

	public boolean isStopped() { return state == SoundState.STOP; }
	public boolean isPlaying() { return state == SoundState.PLAY; }
	public boolean isPaused() { return state == SoundState.PAUSE; }
}

// Gestore del suono
public class SoundManager
{
	// Pool di suoni
	private HashMap<String, Sound> sounds;

	public SoundManager()
	{
		sounds = new HashMap<String, Sound>();
	}

	// Loads a sound
	public void add(final String path)
	{
		final Sound snd = new Sound(path);
		final String name = new File(path).getName();

		sounds.put(name, snd);
	}

	// Shortcuts
	public void play(final Sound sound) { sound.instance().play();	}
	public void stop(final Sound sound) { sound.cleanup(); }
	public void play(final String name) { play(sounds.get(name)); }
	public void stop(final String name) { stop(sounds.get(name));	}

	// Cleanup method
	public void cleanup()
	{
		for (Sound s : sounds.values())
			s.cleanup();
	}

	// Get methods
	public Sound getSound(final String name) { return sounds.get(name); }
}