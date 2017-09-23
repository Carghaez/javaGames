package javaGames.Wali.v1.engine;

import java.util.*;
import java.net.*;
import java.io.*;
import javazoom.jl.decoder.*;
import javazoom.jl.player.*;

// Stato di un'istanza del suono
enum SoundState { STOP, PLAY, PAUSE };

// Suono
class Sound
{
	// Compressed data
	private byte []data;

	// Instances
	private Collection<SoundInstance> instances;

	// Carica un suono da file
	public Sound(final String fileName)
	{
		instances = new LinkedList<SoundInstance>();

		try
		{
			final URL url = FileUtils.getURL(fileName);
			data = FileUtils.getBytes(url);
		}
		catch (java.io.IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}

	public SoundInstance instance()
	{
		// Get an available instance
		SoundInstance newInstance = null;

		for (SoundInstance curInst : instances)
			if (curInst.isStopped())
			{
				newInstance = curInst;
				break;
			}

		if (newInstance == null)
		{
			newInstance = new SoundInstance(this);

			// Add the new instance
			instances.add(newInstance);
		}

		return newInstance;
	}

	public void stopAll()
	{
		for (SoundInstance inst : instances)
			inst.stop();
	}

	public void cleanup()
	{
		for (SoundInstance inst : instances)
			inst.cleanup();

		instances.clear();
	}

	// Get methods
	public byte[] getData() { return data; }
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
	private CustomPlayer player;
	private BufferedInputStream is;

	public SoundInstance(final Sound soundRef)
	{
		this.soundRef = soundRef;
		state = SoundState.STOP;
	}

	public void stop()
	{
		if (state == SoundState.PLAY)
			player.stopNow();
	}

	public void play()
	{
		if (state == SoundState.STOP)
		{
			// Start playing
			state = SoundState.PLAY;

			try
			{
				is = new BufferedInputStream(new ByteArrayInputStream(soundRef.getData()));
				player = new CustomPlayer(is);

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
										e.printStackTrace();
									}
									finally
									{
										player.close();

										player = null;
										is = null;

										state = SoundState.STOP;
									}
								}
							};

				playerThread.start();
			}
			catch (Exception e)
			{
				e.printStackTrace();
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
	public void play(final Sound sound) { sound.instance().play(); }
	public void stop(final Sound sound) { sound.cleanup(); }
	public void play(final String name) { play(sounds.get(name)); }
	public void stop(final String name) { stop(sounds.get(name));	}

	public void stopAll()
	{
		for (Sound s : sounds.values())
			s.stopAll();
	}

	// Cleanup method
	public void cleanup()
	{
		for (Sound s : sounds.values())
			s.cleanup();

		sounds.clear();
	}

	// Get methods
	public Sound getSound(final String name) { return sounds.get(name); }
}

class CustomPlayer
{
	private int frame = 0;

	private Bitstream bitstream;
	private Decoder decoder;
	private AudioDevice audio;

	private boolean	closed = false;
	private boolean mustStop = false;

	private boolean	complete = false;

	private int	lastPosition = 0;

	public CustomPlayer(InputStream stream) throws JavaLayerException
	{
		this(stream, null);
	}

	public CustomPlayer(InputStream stream, AudioDevice device) throws JavaLayerException
	{
		mustStop = false;
		bitstream = new Bitstream(stream);
		decoder = new Decoder();

		if (device!=null)
			audio = device;
		else
		{
			FactoryRegistry r = FactoryRegistry.systemRegistry();
			audio = r.createAudioDevice();
		}

		audio.open(decoder);
	}

	public synchronized void play() throws JavaLayerException
	{
		mustStop = false;
		play(Integer.MAX_VALUE);
	}

	public synchronized boolean play(int frames) throws JavaLayerException
	{
		boolean ret = true;

		while (frames-- > 0 && ret && !mustStop)
			ret = decodeFrame();

		if (!ret)
		{
			// last frame, ensure all data flushed to the audio device.
			AudioDevice out = audio;
			if (out!=null)
			{
				out.flush();
				synchronized (this)
				{
					complete = (!closed);
					close();
				}
			}
		}
		return ret;
	}

	public void stopNow()
	{
		mustStop = true;
	}

	public synchronized void close()
	{
		AudioDevice out = audio;
		if (out!=null)
		{
			closed = true;
			audio = null;
			// this may fail, so ensure object state is set up before
			// calling this method.
			out.close();
			lastPosition = out.getPosition();
			try
			{
				bitstream.close();
			}
			catch (BitstreamException ex)
			{
			}
		}
	}

	public synchronized boolean isComplete()
	{
		return complete;
	}

	public int getPosition()
	{
		int position = lastPosition;

		AudioDevice out = audio;
		if (out!=null)
		{
			position = out.getPosition();
		}
		return position;
	}

	protected boolean decodeFrame() throws JavaLayerException
	{
		try
		{
			AudioDevice out = audio;
			if (out==null)
				return false;

			Header h = bitstream.readFrame();

			if (h==null)
				return false;

			// sample buffer set when decoder constructed
			SampleBuffer output = (SampleBuffer)decoder.decodeFrame(h, bitstream);

			synchronized (this)
			{
				out = audio;
				if (out!=null)
				{
					out.write(output.getBuffer(), 0, output.getBufferLength());
				}
			}

			bitstream.closeFrame();
		}
		catch (RuntimeException ex)
		{
			throw new JavaLayerException("Exception decoding audio frame", ex);
		}

		return true;
	}
}