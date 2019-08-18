package jp.gr.java_conf.raionkun;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Efx4 extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	ParticleEffect effect;
	OrthographicCamera camera;

    ParticleEffectPool effectPool;
    PooledEffect pooledEffect;

    Array<ParticleEffectPool.PooledEffect> effectArray = new Array();
    Vector3 touchPos;
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
        touchPos = new Vector3();

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		effect = new ParticleEffect();
		effect.load(Gdx.files.internal("hoshi.p"), Gdx.files.internal(""));

        effect.setEmittersCleanUpBlendFunction(false);
        effectPool = new ParticleEffectPool(effect,1,2);

	}

	@Override
	public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        batch.setProjectionMatrix(camera.combined);

        // Create effect:
        pooledEffect = effectPool.obtain();
        // process user input
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            pooledEffect.setPosition(touchPos.x, touchPos.y);
            effectArray.add(pooledEffect);
        }

        batch.begin();
        for (int i = effectArray.size - 1; i >= 0; i--) {
            pooledEffect = effectArray.get(i);
            pooledEffect.update(Gdx.graphics.getDeltaTime());
            pooledEffect.draw(batch);
            if (pooledEffect.isComplete()) {
                pooledEffect.free();
                effectArray.removeIndex(i);
            }
        }
        batch.end();

//		if (effect.isComplete()){
//			effect.reset();
//		}
    }
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		effect.dispose();
	}
}
