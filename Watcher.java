import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

public class Watcher {
    public static void main(String[] args) {
        try(WatchService service = FileSystems.getDefault().newWatchService()){
            Map<WatchKey, Path> keyMap = new HashMap<>();
            Path path = Paths.get("directory");
            keyMap.put(path.register(service,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY),
                    path);

            WatchKey watchKey;


            do{
                watchKey = service.take();
                Path eventDir = keyMap.get(watchKey);

                for (WatchEvent<?> event : watchKey.pollEvents()){
                    WatchEvent.Kind<?> kind = event.kind();
                    Path eventPath = (Path) event.context();
                    System.out.println(eventDir + ": " + kind + ": " + eventPath + " at " + LocalDateTime.now());

                    ArrayList<String> linesReader =  new ArrayList<>();
                    ArrayList<String> backupLinesReader = new ArrayList<>();


                    //Creating backup file
                    File originalFile = new File(eventDir.toAbsolutePath().toString() + "//" + eventPath.toString());
                    File newFile = new File(eventDir.toAbsolutePath().toString() + "//" + "Backup.txt");

                    try{
                        if(!newFile.exists()){
                            Files.copy(originalFile.toPath(), newFile.toPath());
                        }else{
                            newFile.deleteOnExit();
                        }

                    }catch(Exception e){
                        e.printStackTrace();
                    }


                    try{
                        BufferedReader originalReader = new BufferedReader(new FileReader((eventDir.toAbsolutePath().toString()+"//"+eventPath.toString())));
                        BufferedReader backupReader = new BufferedReader(new FileReader((eventDir.toAbsolutePath().toString() + "//" + "Backup.txt")));
                        String originalLine = originalReader.readLine();
                        String backupLine = backupReader.readLine();
                        boolean areEqual = true;
                        int lineNum = 1;

                        while (originalLine != null || backupLine != null){
                            if (originalLine == null) {
                                areEqual = false;
                                break;
                            }else if(!originalLine.equalsIgnoreCase(backupLine)){
                                areEqual = false;
                                System.out.println("Line no: " + lineNum + " modified with: " + originalLine);

                            }
                            originalLine = originalReader.readLine();
                            backupLine = backupReader.readLine();
                            lineNum++;
                        }
                        if(areEqual){
                            System.out.println("0 changes identified!");
                        }
                        originalReader.close();
                        backupReader.close();

                    }catch (IOException e){
                        e.printStackTrace();
                    }


                }

            }while (watchKey.reset());

        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
