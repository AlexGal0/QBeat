package android.bignerdranch.com.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class PlayRecipe extends FragmentActivity {
    public Receta receta;
    public FrameLayout container;

    ArrayList<Paso> pasos;

    private ImageView image;
    private FloatingActionButton back;
    private FloatingActionButton next;
    private FloatingActionButton sound;

    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_recipe_layout);

        receta = DataBase.getDataBase().receta;
        pasos = receta.getPasos();

        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, 12);

        image = findViewById(R.id.image_view_play_recipe);
        container = findViewById(R.id.step_play_container);
        back = findViewById(R.id.back_step_play);
        next = findViewById(R.id.next_step_play);
        sound = findViewById(R.id.sound_step_play);

        viewPager = new ViewPageFragment(this);
        viewPager.setId(R.id.view_pager_2);
        container.addView(viewPager);


        FragmentManager fm = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                return SimpleFragmentStep.newInstance(position, pasos.get(position).getDescription());
            }

            @Override
            public int getCount() {
                return pasos.size();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                if(position == pasos.size()-1)
                    next.setImageDrawable(getDrawable(android.R.drawable.ic_menu_myplaces));
                else
                    next.setImageDrawable(getDrawable(android.R.drawable.ic_media_next));
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });


        if(receta.getImage() != null)
            image.setImageBitmap(Util.fixSizeRectangle(receta.getImage()));
        else
            image.setImageBitmap(Util.fixSizeRectangle(DataBase.getDataBase().f));

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = viewPager.getCurrentItem();
                if(index + 1 < pasos.size())
                    viewPager.setCurrentItem(index + 1);
                if(index == pasos.size() -1){
                    AlertDialog.Builder builder = new AlertDialog.Builder(PlayRecipe.this);

                    builder.setTitle("¿Desea finalizar la receta?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();

                            Usuario usuario = DataBase.getDataBase().currentUser;

                            if(!usuario.id.equals("5f2929fe-09cf-4677-bb9a-28a5c15b228c")) {
                                double randomEXP = Math.random()*100.0/2.0;
                                usuario.addExperience(randomEXP);
                                DataBase.getDataBase().addUser(usuario);
                                Toast.makeText(PlayRecipe.this, "Felicidades: +" + randomEXP+ ".", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(PlayRecipe.this, "Registrese para ganar experiencia", Toast.LENGTH_SHORT).show();

                        }
                    });
                    builder.setNegativeButton("No", null);
                    builder.show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = viewPager.getCurrentItem();
                if(index >= 1)
                    viewPager.setCurrentItem(index - 1);
            }
        });

        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mTts.isSpeaking()){
                    mTts.stop();
                }
                else{
                    int index = viewPager.getCurrentItem();
                    String read = pasos.get(index).getDescription();
                    mTts.speak(read, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }
        });
    }

    private TextToSpeech mTts;
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        if (requestCode == 12) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
                mTts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            if (mTts != null) {
                                int result = mTts.setLanguage(Locale.getDefault());
                                if (result == TextToSpeech.LANG_MISSING_DATA || result ==
                                        TextToSpeech.LANG_NOT_SUPPORTED) {
                                    Toast.makeText(PlayRecipe.this, "TTS language is not supported",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    // Do something here
                                }
                            }
                        } else {
                            Toast.makeText(PlayRecipe.this, "TTS initialization failed",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(
                        TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }
}
