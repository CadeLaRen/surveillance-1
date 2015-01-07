import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import au.edu.jcu.v4l4j.CaptureCallback;
import au.edu.jcu.v4l4j.FrameGrabber;
import au.edu.jcu.v4l4j.V4L4JConstants;
import au.edu.jcu.v4l4j.VideoDevice;
import au.edu.jcu.v4l4j.VideoFrame;
import au.edu.jcu.v4l4j.exceptions.StateException;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;

import com.cvezga.surveillance.camera.Camera;

/**
 * This class demonstrates how to perform a simple push-mode capture. It starts
 * the capture and display the video stream in a JLabel
 * 
 * @author gilles
 * 
 *         https://code.google.com/p/v4l4j/wiki/Examples Both of the above can
 *         be done by passing the following arguments to the JVM:
 *         -Djava.library.path=/usr/lib/jni -cp /usr/share/java/v4l4j.jar
 */
public class SimpleViewer extends WindowAdapter implements CaptureCallback {
	private static int width = 640, height = 480,
			std = V4L4JConstants.STANDARD_WEBCAM, channel = 0;
	private static String device = "/dev/video0";

	private VideoDevice videoDevice;
	private FrameGrabber frameGrabber;

	private JLabel label;
	private JFrame frame;

	private Camera camera = new Camera(640, 480, 10, 10);

	private Clip clip;

	public static void main(String args[]) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new SimpleViewer();
			}
		});
	}

	/**
	 * Builds a WebcamViewer object
	 * 
	 * @throws V4L4JException
	 *             if any parameter if invalid
	 */
	public SimpleViewer() {

		InputStream is = getClass().getResourceAsStream("/Bleep.wav");
		AudioInputStream sound;
		try {
			sound = AudioSystem.getAudioInputStream(is);
			clip = AudioSystem.getClip();
			clip.open(sound);
		} catch (UnsupportedAudioFileException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Initialise video device and frame grabber
		try {
			initFrameGrabber();
		} catch (V4L4JException e1) {
			System.err.println("Error setting up capture");
			e1.printStackTrace();

			// cleanup and exit
			cleanupCapture();
			return;
		}

		// create and initialise UI
		initGUI();

		// start capture
		try {
			frameGrabber.startCapture();
		} catch (V4L4JException e) {
			System.err.println("Error starting the capture");
			e.printStackTrace();
		}
	}

	/**
	 * Initialises the FrameGrabber object
	 * 
	 * @throws V4L4JException
	 *             if any parameter if invalid
	 */
	private void initFrameGrabber() throws V4L4JException {
		videoDevice = new VideoDevice(device);
		frameGrabber = videoDevice.getJPEGFrameGrabber(width, height, channel,
				std, 80);
		frameGrabber.setCaptureCallback(this);
		width = frameGrabber.getWidth();
		height = frameGrabber.getHeight();
		System.out.println("Starting capture at " + width + "x" + height);
	}

	/**
	 * Creates the UI components and initialises them
	 */
	private void initGUI() {
		frame = new JFrame();
		label = new JLabel();
		frame.getContentPane().add(label);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(this);
		frame.setVisible(true);
		frame.setSize(width, height);
	}

	/**
	 * this method stops the capture and releases the frame grabber and video
	 * device
	 */
	private void cleanupCapture() {
		try {
			frameGrabber.stopCapture();
		} catch (StateException ex) {
			// the frame grabber may be already stopped, so we just ignore
			// any exception and simply continue.
		}

		// release the frame grabber and video device
		videoDevice.releaseFrameGrabber();
		videoDevice.release();
	}

	/**
	 * Catch window closing event so we can free up resources before exiting
	 * 
	 * @param e
	 */
	public void windowClosing(WindowEvent e) {
		cleanupCapture();

		// close window
		frame.dispose();
	}

	@Override
	public void exceptionReceived(V4L4JException e) {
		// This method is called by v4l4j if an exception
		// occurs while waiting for a new frame to be ready.
		// The exception is available through e.getCause()
		e.printStackTrace();
	}

	@Override
	public void nextFrame(VideoFrame frame) {
		// System.out.println(new Date());
		// This method is called when a new frame is ready.
		// Don't forget to recycle it when done dealing with the frame.

		BufferedImage bimage = camera.updateImage(frame.getBufferedImage());

		// draw the new frame onto the JLabel
		label.getGraphics().drawImage(bimage, 0, 0, width, height, null);

		if (camera.isActivated()) {
			saveImage(frame.getBufferedImage());
			playSound();
		}

		// recycle the frame
		frame.recycle();
	}

	private void saveImage(BufferedImage bimage) {

		try {
			ImageIO.write(
					bimage,
					"jpg",
					new File("/tmp/video" + System.currentTimeMillis() + ".jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void playSound() {
		clip.setFramePosition(0); // Must always rewind!
		clip.start();

	}
}