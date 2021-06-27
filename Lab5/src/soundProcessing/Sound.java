package soundProcessing;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sound {

    public static void play(String fileName) {
        try {
            String filePath = new File("").getAbsolutePath();

            String sep = File.separator;
            File soundFile =  new File(filePath + sep + "sound" + sep + fileName); //Звуковой файл

            //Получаем AudioInputStream
            //Вот тут могут полететь IOException и UnsupportedAudioFileException
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);

            //Получаем реализацию интерфейса Clip
            //Может выкинуть LineUnavailableException
            Clip clip = AudioSystem.getClip();

            //Загружаем наш звуковой поток в Clip
            //Может выкинуть IOException и LineUnavailableException
            clip.open(ais);

            clip.setFramePosition(0); //устанавливаем указатель на старт
            clip.start(); //Поехали!!!

            //Если не запущено других потоков, то стоит подождать, пока клип не закончится
            //В GUI-приложениях следующие 3 строчки не понадобятся
            Thread.sleep(clip.getMicrosecondLength()/1000);
            clip.stop(); //Останавливаем
            clip.close(); //Закрываем
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exc) {
            exc.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Here could be sound...");
        } catch (InterruptedException exc) {
            exc.printStackTrace();
        }
    }

    public static void playError() {
        Sound.play("error.wav");
    }

    public static void playDone() {
        Sound.play("done.wav");
    }

    public static void playEOF() {
        Sound.play("EOF.wav");
    }
    public static void playExit() {
        Sound.play("exit.wav");
    }
}
