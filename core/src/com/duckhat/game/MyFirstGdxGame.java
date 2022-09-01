package com.duckhat.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MyFirstGdxGame extends ApplicationAdapter {

	private Texture dropImage;
	private Texture bucketImage;
	private Sound dropSound;
	private Music rainMusic;

	private OrthographicCamera camera;
	private SpriteBatch batch;

	private Rectangle bucket;
	private Array<Rectangle> raindrops;
	private long lastDropTime;


	@Override
	public void create () {
		// load the images for the droplet and the bucket, 64x64 pixels each
		dropImage = new Texture(Gdx.files.internal("droplet.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));

		// load the drop sound effect and the rain background "music"
		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		// start the playback of the background music immediately
		rainMusic.setLooping(true);
		rainMusic.play();

		//setting camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		//sprite batch
		batch = new SpriteBatch();

		//setting the bucket
		bucket = new Rectangle();
		bucket.x = (800 / 2) - (64 / 2);
		bucket.y = 20;
		bucket.width = 64;
		bucket.height = 64;

		//setting the raindrop
		raindrops = new Array<Rectangle>();
		spawnRaindrop();
	}

	@Override
	public void render () {
		//clear background screen
		ScreenUtils.clear(0, 0, 0.2f, 1);

		//refresh camera
		camera.update();

		//preparing to render
		batch.setProjectionMatrix(camera.combined);
		batch.begin();    //start of opengl stuff
		batch.draw(bucketImage, bucket.x, bucket.y); 	//render of bucket
		for(Rectangle raindrop: raindrops) {			//render raindrops
			batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		batch.end();    //end of opengl stuff

		// getting and processing mouse input
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - (64 / 2);
		}
		// getting and processing keyboard input
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();

		//keep bucket in screen limit
		if (bucket.x < 0) bucket.x = 0;
		if (bucket.x > 800 - 64) bucket.x = 800 - 64;

		//spawn raindrop from time to time
		if (TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();

		//moving raindrop to bottom of screen
		for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if(raindrop.y + 64 < 0) iter.remove();

			//if hits the bucket
			if(raindrop.overlaps(bucket)) {
				dropSound.play();
				iter.remove();
			}
		}
	}

	@Override
	public void dispose() {
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
		batch.dispose();
	}

	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 800-64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}
}
