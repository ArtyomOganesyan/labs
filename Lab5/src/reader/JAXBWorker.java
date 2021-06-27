package reader;

import data.Route;
import data.Routes;

import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.*;

public class JAXBWorker {

    public static Routes generateObject(File file) throws JAXBException, IOException, NumberFormatException {

        FileInputStream fis = new FileInputStream(file) ;
        BufferedInputStream bis = new BufferedInputStream(fis);

        JAXBContext context = JAXBContext.newInstance(Routes.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        Routes routes = (Routes) unmarshaller.unmarshal(bis);

        for (Map.Entry entry: routes.getHashOfRoutes().entrySet()) {
            Route route = routes.getHashOfRoutes().get(entry.getKey());
            if (Reader.checkNumber(route.getDistance(), 1, -1)) throw new NumberFormatException();
            if (Reader.checkNumber(route.getCoordinatesY(), 0, 248)) throw new NumberFormatException();
        }

        return routes;
    }

    public static void generateXML(Routes collection, String filePath) throws JAXBException, IOException {
        File file = new File(filePath);
        FileOutputStream fos = new FileOutputStream(file);

        JAXBContext context = JAXBContext.newInstance(Routes.class);
        Marshaller marshaller = context.createMarshaller();
        // устанавливаем флаг для читабельного вывода XML в JAXB
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        // маршаллинг объекта в файл
        marshaller.marshal(collection, fos);
        fos.close();
    }

    public static Routes listToHash(File file) throws JAXBException, IOException {

        Routes routes = JAXBWorker.generateObject(file);

        List<Route> list = routes.getListOfRoutes();
        HashMap<Integer, Route> hash = routes.getHashOfRoutes();

        Object [] obj = list.toArray();
        for (int i = 1; i <= obj.length; i++) {
            hash.put(i, (Route) obj[i-1]);
            ((Route) obj[i-1]).setId(i);
            ((Route) obj[i-1]).setCreationDate(LocalDateTime.now());
        }

        return routes;
    }
}
