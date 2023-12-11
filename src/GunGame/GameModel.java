package GunGame;

import GunGame.GameLogic.BulletModel;
import GunGame.GameLogic.ManModel;
import GunGame.GameLogic.ZombieModel;
import com.sun.opengl.util.j2d.TextRenderer;
import java.awt.Color;
import java.awt.Font;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;

public class GameModel extends AnimListener implements GLEventListener, MouseListener, MouseMotionListener, KeyListener {

    TextRenderer ren = new TextRenderer(new Font("sanaSerif", Font.BOLD, 10));
    GL gl;
    float rotatedX, rotatedY;
    int maxWidth = 1280, maxHeight = 720, numberOfkillsPlayer1, numberOfkillsPlayer2, soundIdx, delayFire, Zombidx,
            manidx, zombiedelay, counter, time, delayTime, zombieSpeed, delayStartGame,
            HealthIndexMan1, HealthIndexMan2, directionMan1, directionMan2, delayFinshGame;
    boolean sound = true, fireMan1, fireMan2, isFeetMan1, isFeetMan2, pause;
    String page = "home", mode;//EasyLevel
    AudioInputStream audioStream1, audioButtonStream, audioZombiesStream, audioFireStream, audioManFeetStream;
    Clip clip, clip2, clip3, clip4, clip5;
    ArrayList<BulletModel> bulletsMan1 = new ArrayList<>();
    ArrayList<BulletModel> bulletsMan2 = new ArrayList<>();

    String textureNames[] = {"soundOnWhite", "soundOffWhite", "playButton", "HowToPlayButton",
        "CreditsButton", "quitButton", "SinglePlaerButton", "MulitiPlayerButton",
        "BackButton_1", "EasyLevelButton", "MediumButton", "HardButton",
        "Menu", "Ground2", "Zombie_Arms_Blood", "blood", "ScreenSplat", "Midnight",
        "Notitz", "PaperHowToPlay", "Ground", "arrowLeft", "Stone_Floor", "desertGround", "Dirt_2",
        "Ground3", "skeleton-move_0", "skeleton-move_1", "skeleton-move_2", "skeleton-move_3", "skeleton-move_4", "skeleton-move_5",
        "skeleton-move_6", "skeleton-move_7", "skeleton-move_8", "skeleton-move_9", "skeleton-move_10", "skeleton-move_11",
        "skeleton-move_12", "skeleton-move_13", "skeleton-move_14", "skeleton-move_15", "skeleton-move_16", "Healthbar",
        "B", "gear", "PauseMenu", "survivor-move_rifle_0", "survivor-move_rifle_1", "survivor-move_rifle_2",
        "survivor-move_rifle_3", "survivor-move_rifle_4", "survivor-move_rifle_5", "survivor-move_rifle_6",
        "survivor-move_rifle_7", "survivor-move_rifle_8", "survivor-move_rifle_9", "survivor-move_rifle_10",
        "survivor-move_rifle_11", "survivor-move_rifle_12", "survivor-move_rifle_13",
        "survivor-move_rifle_14", "survivor-move_rifle_15", "survivor-move_rifle_16", "survivor-move_rifle_17",
        "survivor-move_rifle_18", "survivor-move_rifle_19", "AK47MuzzleFlash", "BulletTrail", "bullet3", "blood",
        "Healthbar100", "Healthbar75", "Healthbar50", "Healthbar25", "Box", "survivor-move_rifle_0_man2",
        "survivor-move_rifle_1_man2", "survivor-move_rifle_2_man2", "survivor-move_rifle_3_man2",
        "survivor-move_rifle_4_man2", "survivor-move_rifle_5_man2", "survivor-move_rifle_6_man2",
        "survivor-move_rifle_7_man2", "survivor-move_rifle_8_man2", "survivor-move_rifle_9_man2",
        "survivor-move_rifle_10_man2", "survivor-move_rifle_11_man2", "survivor-move_rifle_12_man2",
        "survivor-move_rifle_13_man2",
        "survivor-move_rifle_14_man2", "survivor-move_rifle_15_man2", "survivor-move_rifle_16_man2",
        "survivor-move_rifle_17_man2", "survivor-move_rifle_18_man2", "survivor-move_rifle_19_man2",
        "Healthbar100_2", "Healthbar75_2", "Healthbar50_2", "Healthbar25_2", "HSOverlay", "NextButton", "GameOver"};
    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
    int textures[] = new int[textureNames.length];

    int[] ZombiMove = {26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42};
    int[] ManMove = {47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65};
    int[] ManMove2 = {76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95};
    int[] HealthBar = {71, 72, 73, 74, 43};
    int[] HealthBar2 = {96, 97, 98, 99, 43};

    ManModel man = new ManModel(50, 50);
    ManModel man2 = new ManModel(50, 400);
    ArrayList<ZombieModel> zombies = new ArrayList<>();
    BulletModel directionBullet1 = new BulletModel(70, -17, 100, 0);
    BulletModel directionBullet2 = new BulletModel(70, -17, 100, 0);

    @Override
    public void init(GLAutoDrawable glad) {
        for (int i = 0; i < textureNames.length; i++) {
            System.out.println(i + " = " + textureNames[i]);
        }
        try {
            audioStream1 = AudioSystem.getAudioInputStream(new File("Assets//songs//space.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioStream1);
            //clip.start();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        try {
            audioButtonStream = AudioSystem.getAudioInputStream(new File("Assets//songs//click_sound_1.wav"));
            clip2 = AudioSystem.getClip();
            clip2.open(audioButtonStream);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        try {
            audioZombiesStream = AudioSystem.getAudioInputStream(new File("Assets//songs//sound__zombie.wav"));
            clip3 = AudioSystem.getClip();
            clip3.open(audioZombiesStream);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        try {
            audioFireStream = AudioSystem.getAudioInputStream(new File("Assets//songs//shootingSound_rifle3.wav"));
            clip4 = AudioSystem.getClip();
            clip4.open(audioFireStream);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        try {
            audioManFeetStream = AudioSystem.getAudioInputStream(new File("Assets//songs//SnowWalk2.wav"));
            clip5 = AudioSystem.getClip();
            clip5.open(audioManFeetStream);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        gl = glad.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);    //This Will Clear The Background Color To Black
        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + textureNames[i] + ".png", true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);

                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA, // Internal Texel Format,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA, // External format from image,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels() // Imagedata
                );
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
        fillZombiesArray();
    }

    @Override
    public void display(GLAutoDrawable glad) {
        gl = glad.getGL();
        //clip.start();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();
        switch (page) {
            case "home":
                DrawHomePage();
                ren.setColor(Color.WHITE);
                break;
            case "Play":
                DrawPlayPage();
                DrawObject(10, 650, 1.0, 1.0, 0, 21);
                ren.setColor(Color.WHITE);
                break;
            case "SinglePlayer":
                levelsButton();
                DrawObject(10, 650, 1.0, 1.0, 0, 21);
                ren.setColor(Color.WHITE);
                break;
            case "MulitiPlayer":
                levelsButton();
                DrawObject(10, 650, 1.0, 1.0, 0, 21);
                ren.setColor(Color.WHITE);
                break;
            case "HowPlayButton":
                DrawBackground(12);
                DrawObject(400, 360, 5.0, 7.0, 0, 19);
                DrawObject(400, 70, 2.0, 1.0, 0, 8);
                DrawObject(10, 650, 1.0, 1.0, 0, 21);
                ren.setColor(Color.WHITE);
                break;
            case "CreditsButton":
                DrawBackground(12);
                DrawObject(200, 340, 4.0, 8.0, 0, 18);
                DrawObject(200, 50, 2.0, 1.0, 0, 8);
                DrawObject(10, 650, 1.0, 1.0, 0, 21);
                ren.setColor(Color.WHITE);
                break;
            case "EasyLevel":
                if (mode == "Muliti") {
                    DrawBackground(25);//22
                    //DrawObject(250, 650, 3.0, 0.5, 0, 43);
                    //DrawObject(10, 650, 1.0, 1.0, 0, 21);
                    DrawMan1();
                    DrawMan2();
                    DrawZombAndiMoved();
                    killMan1();
                    killMan2();
                    zombieAttack();
                    DrawHealthbarPlayer1();
                    DrawHealthbarPlayer2();
                    DrawObject(660, 640, 1.5, 0.8, 0, 75);
                    drawTime();
                    DrawObject(740, 585, 1.2, 0.6, 0, 75);
                    drawNumKillsPlayer1();
                    DrawObject(580, 585, 1.2, 0.6, 0, 75);
                    drawNumKillsPlayer2();
                    DrawObject(1150, 630, 0.75, 0.75, 0, 44);
                    ren.setColor(Color.WHITE);
                    if (pause) {
                        DrawObject(600, 350, 4.0, 7.0, 0, 46);
                        ren.beginRendering(300, 300);
                        //ren.setColor(Color.GRAY);
                        ren.endRendering();
                    }
                } else {
                    DrawBackground(25);//22
                    //DrawObject(250, 650, 3.0, 0.5, 0, 43);
                    //DrawObject(10, 650, 1.0, 1.0, 0, 21);
                    DrawMan1();
                    DrawZombAndiMoved();
                    killMan1();
                    zombieAttack();
                    DrawHealthbarPlayer1();
                    DrawObject(660, 640, 1.5, 0.8, 0, 75);
                    drawTime();
                    DrawObject(660, 585, 1.5, 0.6, 0, 75);
                    drawNumKillsPlayer1();
                    DrawObject(1150, 630, 0.75, 0.75, 0, 44);
                    ren.setColor(Color.WHITE);
                    if (pause) {
                        DrawObject(600, 350, 4.0, 7.0, 0, 46);
                        ren.beginRendering(300, 300);
                        //ren.setColor(Color.GRAY);
                        ren.endRendering();
                    }
                    if (numberOfkillsPlayer1 == 3) {
                        page = "Win";
                    }
                }
                break;
            case "MediumLevel":
                if (mode == "Muliti") {
                    DrawBackground(13);//22
                    //DrawObject(250, 650, 3.0, 0.5, 0, 43);
                    //DrawObject(10, 650, 1.0, 1.0, 0, 21);
                    DrawMan1();
                    DrawMan2();
                    DrawZombAndiMoved();
                    killMan1();
                    killMan2();
                    zombieAttack();
                    DrawHealthbarPlayer1();
                    DrawHealthbarPlayer2();
                    DrawObject(660, 640, 1.5, 0.8, 0, 75);
                    drawTime();
                    DrawObject(660, 585, 1.2, 0.6, 0, 75);
                    drawNumKillsPlayer1();
                    DrawObject(1150, 630, 0.75, 0.75, 0, 44);
                    ren.setColor(Color.WHITE);
                    if (pause) {
                        DrawObject(600, 350, 4.0, 7.0, 0, 46);
                        ren.beginRendering(300, 300);
                        //ren.setColor(Color.GRAY);
                        ren.endRendering();
                    }
                } else {
                    DrawBackground(13);//22
                    //DrawObject(250, 650, 3.0, 0.5, 0, 43);
                    //DrawObject(10, 650, 1.0, 1.0, 0, 21);
                    DrawMan1();
                    DrawZombAndiMoved();
                    killMan1();
                    zombieAttack();
                    DrawHealthbarPlayer1();
                    DrawObject(660, 640, 1.5, 0.8, 0, 75);
                    drawTime();
                    DrawObject(660, 585, 1.5, 0.6, 0, 75);
                    drawNumKillsPlayer1();
                    DrawObject(1150, 630, 0.75, 0.75, 0, 44);
                    ren.setColor(Color.WHITE);
                    if (pause) {
                        DrawObject(600, 350, 4.0, 7.0, 0, 46);
                        ren.beginRendering(300, 300);
                        //ren.setColor(Color.GRAY);
                        ren.endRendering();
                    }
                }
                break;
            case "HardLevel":
                if (mode == "Muliti") {
                    DrawBackground(20);//22
                    //DrawObject(250, 650, 3.0, 0.5, 0, 43);
                    //DrawObject(10, 650, 1.0, 1.0, 0, 21);
                    DrawMan1();
                    DrawMan2();
                    DrawZombAndiMoved();
                    killMan1();
                    killMan2();
                    zombieAttack();
                    DrawHealthbarPlayer1();
                    DrawHealthbarPlayer2();
                    DrawObject(660, 640, 1.5, 0.8, 0, 75);
                    drawTime();
                    DrawObject(660, 585, 1.2, 0.6, 0, 75);
                    drawNumKillsPlayer1();
                    DrawObject(1150, 630, 0.75, 0.75, 0, 44);
                    ren.setColor(Color.WHITE);
                    if (pause) {
                        DrawObject(600, 350, 4.0, 7.0, 0, 46);
                        ren.beginRendering(300, 300);
                        //ren.setColor(Color.GRAY);
                        ren.endRendering();
                    }
                } else {
                    DrawBackground(20);//22
                    //DrawObject(250, 650, 3.0, 0.5, 0, 43);
                    //DrawObject(10, 650, 1.0, 1.0, 0, 21);
                    DrawMan1();
                    DrawZombAndiMoved();
                    killMan1();
                    zombieAttack();
                    DrawHealthbarPlayer1();
                    DrawObject(660, 640, 1.5, 0.8, 0, 75);
                    drawTime();
                    DrawObject(660, 585, 1.5, 0.6, 0, 75);
                    drawNumKillsPlayer1();
                    DrawObject(1150, 630, 0.75, 0.75, 0, 44);
                    ren.setColor(Color.WHITE);
                    if (pause) {
                        DrawObject(600, 350, 4.0, 7.0, 0, 46);
                        ren.beginRendering(300, 300);
                        //ren.setColor(Color.GRAY);
                        ren.endRendering();
                    }
                }
                break;
            case "Win":
                DrawBackground(12);
                DrawBackground(100);
                DrawObject(300, 50, 2.0, 1.0, 0, 8);
                DrawObject(800, 50, 2.0, 1.0, 0, 101);
                break;
            case "Lose":
                DrawObject(600, 300, 1.0, 1.0, 0, 102);
                break;
        }
    }

    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {
    }

    @Override
    public void displayChanged(GLAutoDrawable glad, boolean bln, boolean bln1) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // System.out.println("mouseClick!!");
        // System.out.println(e.getX() + " " + e.getY());
        switch (page) {
            case "home":
                if (e.getX() >= 1150 && e.getX() < 1240 && e.getY() > maxHeight - 700 && e.getY() < maxHeight - 650) {
                    sound = !sound;
                    if (sound) {
                        soundIdx = 0;
                        clip.setMicrosecondPosition(0);
                        clip.start();
                    } else {
                        soundIdx = 1;
                        clip.stop();
                    }
                }
                if (e.getX() >= 40 && e.getX() < 285 && e.getY() > maxHeight - 490 && e.getY() < maxHeight - 425) {
                    page = "Play";
                    clip2.setMicrosecondPosition(0);
                    clip2.start();
                }
                if (e.getX() >= 35 && e.getX() < 285 && e.getY() > maxHeight - 395 && e.getY() < maxHeight - 335) {
                    page = "HowPlayButton";
                    clip2.setMicrosecondPosition(0);
                    clip2.start();
                }
                if (e.getX() >= 35 && e.getX() < 285 && e.getY() > maxHeight - 300 && e.getY() < maxHeight - 240) {
                    page = "CreditsButton";
                    clip2.setMicrosecondPosition(0);
                    clip2.start();
                }
                if (e.getX() >= 35 && e.getX() < 285 && e.getY() > maxHeight - 210 && e.getY() < maxHeight - 145) {
                    clip2.setMicrosecondPosition(0);
                    clip2.start();
                    System.exit(0);
                }
                break;
            case "Play":
                if (sound) {
                    clip.setMicrosecondPosition(0);
                    clip.start();
                }
                if (e.getX() >= 35 && e.getX() < 285 && e.getY() > maxHeight - 490 && e.getY() < maxHeight - 425) {
                    page = "SinglePlayer";
                    mode = "Single";
                    clip2.setMicrosecondPosition(0);
                    clip2.start();
                }
                if (e.getX() >= 35 && e.getX() < 285 && e.getY() > maxHeight - 395 && e.getY() < maxHeight - 335) {
                    page = "MulitiPlayer";
                    mode = "Muliti";
                    clip2.setMicrosecondPosition(0);
                    clip2.start();
                }
                if (e.getX() >= 35 && e.getX() < 285 && e.getY() > maxHeight - 305 && e.getY() < maxHeight - 240) {
                    page = "home";
                    clip2.setMicrosecondPosition(0);
                    clip2.start();
                }
                if (e.getX() > 30 && e.getX() < 115 && e.getY() > maxHeight - 710 && e.getY() < maxHeight - 670) {
                    page = "home";
                    clip2.setMicrosecondPosition(0);
                    clip2.start();
                }
                break;
            case "SinglePlayer":
            case "MulitiPlayer":
                if (sound) {
                    clip.setMicrosecondPosition(0);
                    clip.start();
                }
                if (e.getX() >= 35 && e.getX() < 285 && e.getY() > maxHeight - 490 && e.getY() < maxHeight - 430) {
                    page = "EasyLevel";
                    clip2.setMicrosecondPosition(0);
                    clip2.start();
                }
                if (e.getX() >= 35 && e.getX() < 285 && e.getY() > maxHeight - 400 && e.getY() < maxHeight - 335) {
                    page = "MediumLevel";
                    clip2.setMicrosecondPosition(0);
                    clip2.start();
                }
                if (e.getX() >= 35 && e.getX() < 285 && e.getY() > maxHeight - 305 && e.getY() < maxHeight - 240) {
                    page = "HardLevel";
                    clip2.setMicrosecondPosition(0);
                    clip2.start();
                }
                if (e.getX() >= 35 && e.getX() < 285 && e.getY() > maxHeight - 210 && e.getY() < maxHeight - 145) {
                    page = "Play";
                    clip2.setMicrosecondPosition(0);
                    clip2.start();
                }
                if (e.getX() > 30 && e.getX() < 115 && e.getY() > maxHeight - 710 && e.getY() < maxHeight - 670) {
                    page = "Play";
                    clip2.setMicrosecondPosition(0);
                    clip2.start();
                }
                break;
            case "CreditsButton":
                if (e.getX() > 135 && e.getX() < 385 && e.getY() > maxHeight - 150 && e.getY() < maxHeight - 85) {
                    page = "home";
                    clip2.setMicrosecondPosition(0);
                    clip2.start();
                }
                if (e.getX() > 30 && e.getX() < 115 && e.getY() > maxHeight - 710 && e.getY() < maxHeight - 670) {
                    page = "home";
                    clip2.setMicrosecondPosition(0);
                    clip2.start();
                }
                break;
            case "HowPlayButton":
                if (e.getX() > 335 && e.getX() < 585 && e.getY() > maxHeight - 170 && e.getY() < maxHeight - 105) {
                    page = "home";
                    clip2.setMicrosecondPosition(0);
                    clip2.start();
                }
                if (e.getX() > 30 && e.getX() < 115 && e.getY() > maxHeight - 710 && e.getY() < maxHeight - 670) {
                    page = "home";
                    clip2.setMicrosecondPosition(0);
                    clip2.start();
                }
                break;
            case "EasyLevel":
                if (e.getX() > 30 && e.getX() < 115 && e.getY() > maxHeight - 710 && e.getY() < maxHeight - 670) {
                    if (mode == "Muliti") {
                        page = "MulitiPlayer";
                    } else {
                        page = "SinglePlayer";
                    }
                }
                if (pause) {
                    if (e.getX() > 485 && e.getX() < 835 && e.getY() > maxHeight - 585 && e.getY() < maxHeight - 505) {
                        page = "EasyLevel";
                        pause = false;
                        clip4.start();
                        clip5.start();
                    }
                }
                if (pause) {
                    if (e.getX() > 485 && e.getX() < 835 && e.getY() > maxHeight - 445 && e.getY() < maxHeight - 365) {
                        defaultGame();
                        page = "EasyLevel";
                        pause = false;
                        clip4.start();
                        clip5.start();
                    }
                }
                if (pause) {
                    if (e.getX() > 485 && e.getX() < 835 && e.getY() > maxHeight - 295 && e.getY() < maxHeight - 220) {
                        clip4.stop();
                        clip5.stop();
                        page = "home";
                        pause = false;
                        defaultGame();
                    }
                }
                if (e.getX() > 1170 && e.getX() < 1250 && e.getY() < 70 && e.getY() > 25) {
                    pause = true;
                    System.out.println("pause");
                }

                break;
            case "MediumLevel":
                if (e.getX() > 30 && e.getX() < 115 && e.getY() > maxHeight - 710 && e.getY() < maxHeight - 670) {
                    if (mode == "Muliti") {
                        page = "MulitiPlayer";
                    } else {
                        page = "SinglePlayer";
                    }
                }
                if (pause) {
                    if (e.getX() > 485 && e.getX() < 835 && e.getY() > maxHeight - 585 && e.getY() < maxHeight - 505) {
                        page = "MediumLevel";
                        pause = false;
                        clip4.start();
                        clip5.start();
                    }
                }
                if (pause) {
                    if (e.getX() > 485 && e.getX() < 835 && e.getY() > maxHeight - 445 && e.getY() < maxHeight - 365) {
                        defaultGame();
                        page = "MediumLevel";
                        pause = false;
                        clip4.start();
                        clip5.start();
                    }
                }
                if (pause) {
                    if (e.getX() > 485 && e.getX() < 835 && e.getY() > maxHeight - 295 && e.getY() < maxHeight - 220) {
                        clip4.stop();
                        clip5.stop();
                        page = "home";
                        pause = false;
                        defaultGame();
                    }
                }
                break;
            case "HardLevel":
                if (e.getX() > 30 && e.getX() < 115 && e.getY() > maxHeight - 710 && e.getY() < maxHeight - 670) {
                    if (mode == "Muliti") {
                        page = "MulitiPlayer";
                    } else {
                        page = "SinglePlayer";
                    }
                }
                if (pause) {
                    if (e.getX() > 485 && e.getX() < 835 && e.getY() > maxHeight - 585 && e.getY() < maxHeight - 505) {
                        page = "HardLevel";
                        pause = false;
                        clip4.start();
                        clip5.start();
                    }
                }
                if (pause) {
                    if (e.getX() > 485 && e.getX() < 835 && e.getY() > maxHeight - 445 && e.getY() < maxHeight - 365) {
                        defaultGame();
                        page = "HardLevel";
                        pause = false;
                        clip4.start();
                        clip5.start();
                    }
                }
                if (pause) {
                    if (e.getX() > 485 && e.getX() < 835 && e.getY() > maxHeight - 295 && e.getY() < maxHeight - 220) {
                        clip4.stop();
                        clip5.stop();
                        page = "home";
                        pause = false;
                        defaultGame();
                    }
                }
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!pause && !man.kill) {
            bulletsMan1.add(new BulletModel(man.xMan + directionBullet1.x, man.yMan + directionBullet1.y, directionBullet1.xdirection, directionBullet1.ydirection));
            fireMan1 = true;
            if (page == "EasyLevel" || page == "MediumLevel" || page == "HardLevel") {
                clip4.setMicrosecondPosition(0);
                clip4.start();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        System.out.println(e.getX() + " " + (maxHeight - e.getY()));

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    BitSet keybits = new BitSet(256);

    @Override
    public void keyPressed(KeyEvent e) {
        keybits.set(e.getKeyCode());

        if (!pause) {
            manidx++;
            manidx %= 19;
            if (!man.kill) {
                if (keybits.get(KeyEvent.VK_RIGHT)) {
                    directionMan1 = 0;
                    directionBullet1 = new BulletModel(70, -17, 100, 0);
                    isFeetMan1 = true;
                    clip5.setMicrosecondPosition(0);
                    clip5.start();
                    if (man.xMan < maxWidth - 150) {
                        man.xMan += 5;
                    }
                }
                if (keybits.get(KeyEvent.VK_LEFT)) {
                    directionMan1 = 180;
                    directionBullet1 = new BulletModel(-70, 17, -100, 0);
                    isFeetMan1 = true;
                    clip5.setMicrosecondPosition(0);
                    clip5.start();
                    if (man.xMan > 0) {
                        man.xMan -= 5;
                    }
                }
                if (keybits.get(KeyEvent.VK_UP)) {
                    directionMan1 = 90;
                    directionBullet1 = new BulletModel(30, 40, 0, 100);
                    isFeetMan1 = true;
                    clip5.setMicrosecondPosition(0);
                    clip5.start();
                    if (man.yMan < maxHeight - 100) {
                        man.yMan += 5;
                    }
                }
                if (keybits.get(KeyEvent.VK_DOWN)) {
                    directionMan1 = 270;
                    directionBullet1 = new BulletModel(-30, -40, 0, -100);
                    isFeetMan1 = true;
                    clip5.setMicrosecondPosition(0);
                    clip5.start();
                    if (man.yMan > 0) {
                        man.yMan -= 5;
                    }
                }

                if (keybits.get(KeyEvent.VK_RIGHT) && keybits.get(KeyEvent.VK_UP)) {
                    directionMan1 = 45;
                    directionBullet1 = new BulletModel(60, 10, 100, 100);
                }
                if (keybits.get(KeyEvent.VK_RIGHT) && keybits.get(KeyEvent.VK_DOWN)) {
                    directionBullet1 = new BulletModel(40, -40, 100, -100);
                    directionMan1 = 315;
                }
                if (keybits.get(KeyEvent.VK_LEFT) && keybits.get(KeyEvent.VK_UP)) {
                    directionBullet1 = new BulletModel(-40, 40, -100, 100);
                    directionMan1 = 135;
                }
                if (keybits.get(KeyEvent.VK_LEFT) && keybits.get(KeyEvent.VK_DOWN)) {
                    directionBullet1 = new BulletModel(-70, -20, -100, -100);
                    directionMan1 = 225;
                }
            }
            if (!man2.kill) {
                if (keybits.get(KeyEvent.VK_D)) {
                    directionMan2 = 0;
                    directionBullet2 = new BulletModel(70, -17, 100, 0);
                    isFeetMan2 = true;
                    clip5.setMicrosecondPosition(0);
                    clip5.start();
                    if (man2.xMan < maxWidth - 150) {
                        man2.xMan += 5;
                    }
                }
                if (keybits.get(KeyEvent.VK_A)) {
                    directionMan2 = 180;
                    directionBullet2 = new BulletModel(-70, 17, -100, 0);
                    isFeetMan2 = true;
                    clip5.setMicrosecondPosition(0);
                    clip5.start();
                    if (man2.xMan > 0) {
                        man2.xMan -= 5;
                    }
                }
                if (keybits.get(KeyEvent.VK_W)) {
                    directionMan2 = 90;
                    directionBullet2 = new BulletModel(30, 40, 0, 100);
                    isFeetMan2 = true;
                    clip5.setMicrosecondPosition(0);
                    clip5.start();
                    if (man2.yMan < maxHeight - 100) {
                        man2.yMan += 5;
                    }
                }
                if (keybits.get(KeyEvent.VK_S)) {
                    directionMan2 = 270;
                    directionBullet2 = new BulletModel(-30, -40, 0, -100);
                    isFeetMan2 = true;
                    clip5.setMicrosecondPosition(0);
                    clip5.start();
                    if (man2.yMan > 0) {
                        man2.yMan -= 5;
                    }
                }

                if (keybits.get(KeyEvent.VK_D) && keybits.get(KeyEvent.VK_W)) {
                    directionMan2 = 45;
                    directionBullet2 = new BulletModel(60, 10, 100, 100);
                }
                if (keybits.get(KeyEvent.VK_D) && keybits.get(KeyEvent.VK_S)) {
                    directionBullet2 = new BulletModel(40, -40, 100, -100);
                    directionMan2 = 315;
                }
                if (keybits.get(KeyEvent.VK_A) && keybits.get(KeyEvent.VK_W)) {
                    directionBullet2 = new BulletModel(-40, 40, -100, 100);
                    directionMan2 = 135;
                }
                if (keybits.get(KeyEvent.VK_A) && keybits.get(KeyEvent.VK_S)) {
                    directionBullet2 = new BulletModel(-70, -20, -100, -100);
                    directionMan2 = 225;
                }
                if (keybits.get(KeyEvent.VK_SPACE)) {
                    bulletsMan2.add(new BulletModel(man2.xMan + directionBullet2.x, man2.yMan + directionBullet2.y, directionBullet2.xdirection, directionBullet2.ydirection));
                    fireMan2 = true;
                    clip4.setMicrosecondPosition(0);
                    clip4.start();
                }
            }

            if (keybits.get(KeyEvent.VK_ESCAPE)) {
                pause = !pause;
                System.out.println(pause);
            }
        }
        if (keybits.get(KeyEvent.VK_R)) {
            defaultGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keybits.clear(e.getKeyCode());
        isFeetMan1 = false;
        isFeetMan2 = false;
        fireMan2 = false;
    }


    public void DrawBackground(int index) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);    // Turn Blending On
        //gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        //gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawObject(int x, int y, double scaleX, double scaleY, double degree, int index) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);    // Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scaleX, 0.1 * scaleY, 1);
        gl.glRotated(degree, 0, 0, 1);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }
    
       public void DrawHomePage() {
        DrawBackground(12);
        DrawObject(150, 350, 9.5, 10, 0, 17);
        DrawObject(1150, 630, 0.5, 0.5, 0, soundIdx);
        //Draw Buttons
        int[] array = {2, 3, 4, 5};
        int buttonPostionY = 400;
        for (int i = 0; i < 4; i++) {
            DrawObject(100, buttonPostionY, 2.0, 1.0, 0, array[i]);
            buttonPostionY -= 100;

        }
    }

    public void DrawPlayPage() {
        DrawBackground(12);
        int buttonPostionY2 = 400;
        int array[] = {6, 7, 8};
        for (int i = 0; i < 3; i++) {
            DrawObject(100, buttonPostionY2, 2.0, 1.0, 0, array[i]);
            buttonPostionY2 -= 100;
        }
    }

    public void levelsButton() {
        DrawBackground(12);
        int buttonPostionY2 = 400;
        int[] array = {9, 10, 11, 8};
        for (int i = 0; i < 4; i++) {
            DrawObject(100, buttonPostionY2, 2.0, 1.0, 0, array[i]);
            buttonPostionY2 -= 100;
        }
    }


}
