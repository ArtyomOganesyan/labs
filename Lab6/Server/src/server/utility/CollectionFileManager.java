package server.utility;

import data.Route;
import data.Routes;
import utility.Outputer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class CollectionFileManager {
    private static String env;
    private File file;


    public CollectionFileManager(String env) {
        this.env = env;
        this.file = new File(System.getenv(env));
    }

    public Routes generateObject() {
        if (System.getenv().get(env) != null) {
            try {
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);

                JAXBContext context = JAXBContext.newInstance(Routes.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();

                Routes routes = (Routes) unmarshaller.unmarshal(bis);

                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.marshal(routes, System.out);

                for (Map.Entry entry : routes.getHashOfRoutes().entrySet()) {
                    Route route = routes.getHashOfRoutes().get(entry.getKey());
                    //if (NumberChecking.check(route.getDistance(), 1, -1)) throw new NumberFormatException();
                    //if (NumberChecking.check(route.getCoordinatesY(), 0, 248)) throw new NumberFormatException();
                }

                HashMap<Integer, Route> hash = routes.getHashOfRoutes();

                for (Map.Entry entry : hash.entrySet()) {
                    Route route = hash.get(entry.getKey());
                    route.setCreationDate(LocalDateTime.now());
                    route.setId(Integer.parseInt(entry.getKey().toString()));
                }

                Outputer.println("Коллекция успешно загружена");
                return routes;

            } catch (FileNotFoundException e) {
                Outputer.printerr("Загрузочный файл не найден!");
            } catch (NoSuchElementException e) {
                Outputer.printerr("Загрузочный файл пуст!");
            } catch (JAXBException | NullPointerException e) {
                Outputer.printerr("Загрузочный файл повреждён!");
                e.printStackTrace();
            } catch (NumberFormatException e) {
                Outputer.printerr("В загрузочном файле не обнаружена корректная коллекция!");
                e.printStackTrace();
            } catch (Exception e) {
                Outputer.printerr("Undefined error");
                System.exit(0);
            }
        } else Outputer.printerr("Системная переменная с загрузочным файлом не найдена!");
        return new Routes();
    }


    // Можно в принципе сохранять в один файл, ПОДУМАЙ

    public static void generateXML(Routes collection) throws JAXBException, IOException {
        File file = new File("saved.xml");
        FileOutputStream fos = new FileOutputStream(file);

        JAXBContext context = JAXBContext.newInstance(Routes.class);
        Marshaller marshaller = context.createMarshaller();
        // устанавливаем флаг для читабельного вывода XML в JAXB
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        // маршаллинг объекта в файл
        marshaller.marshal(collection, fos);
        fos.close();
    }
}
