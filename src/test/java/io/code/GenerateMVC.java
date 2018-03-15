package io.code;


import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * @author songkejun
 * @create 2018-02-06 20:21
 **/
public class GenerateMVC {

    private static String projectPath ="D:\\GeneratedSpringProject";
    private static String projectSrcPath;
    private static String webroot;
    private static String controllersPackage = "controllers";
    private static String viewPrefix = "myviews";
    private static int controllersAmount = 3;
    private static int mappingsAmount = 3;
    private static int configsAmount = 3;
    private static int beansAmount = 3;
    private static int compsAmount = 3;
    private static String webconfigPackageName = "webconfig";
    private static String webConfigName = "MyWebConfig";
    private static String appConfigPackage = "appconfig";
    private static String appConfigName = "MainConfig";

    public static void main(String[] args) throws IOException {
        ResourceBundle rb = ResourceBundle.getBundle("generator");
        projectPath = rb.getString("prefix");
        projectSrcPath = projectPath+"\\src\\main\\java\\";
        webroot = projectPath+"\\src\\main\\webapp\\";
        viewPrefix = rb.getString("viewPrefix");
        controllersPackage = rb.getString("controllersPackage");
        controllersAmount = Integer.parseInt(rb.getString("controllersAmount"));
        mappingsAmount = Integer.parseInt(rb.getString("mappingsAmount"));
        configsAmount = Integer.parseInt(rb.getString("configsAmount"));
        beansAmount = Integer.parseInt(rb.getString("beansAmount"));
        compsAmount = Integer.parseInt(rb.getString("compsAmount"));
        generateProject();

    }
    private static void generateProject() throws IOException {
        try {
            if(!new File(projectSrcPath).mkdirs() || !new File(webroot).mkdirs()){
                System.out.println("make sure that the target directory doesn't exist: "+ projectPath);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        File templatePom = new File("\\templates\\pom.txt");
        File templateMainClass = new File(".\\src\\templates\\CheckBeansAmount.txt");
        File destPom = new File(projectPath+"\\pom.xml");
        File destMainClass = new File(projectPath+"\\src\\main\\java\\CheckBeansAmount.java");

        copyTemplateFile(templatePom, destPom);
        copyTemplateFile(templateMainClass, destMainClass);
        generateApplication(projectSrcPath,"myapplication","MyApplication");
        generateConfigs(projectSrcPath,appConfigPackage,configsAmount,appConfigName);
        generateWebConfig(projectSrcPath,webconfigPackageName, webConfigName, controllersPackage, viewPrefix,controllersAmount);

    }
    private static void generateApplication(String prefix, String packageName, String className){
        PrintWriter writer = createClassFile(prefix, packageName, className);
        if (writer != null) {
            writer.println("package " + packageName + ";");
            writer.println("import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;");
            writer.println("public class "+className+ " extends AbstractAnnotationConfigDispatcherServletInitializer {");
            writer.println("@Override\n" +
                    "    protected Class<?>[] getRootConfigClasses() {\n" +
                    "        return new Class<?>[]{"+appConfigPackage+"."+appConfigName+".class};\n" +
                    "    }");
            writer.println("@Override\n" +
                    "    protected Class<?>[] getServletConfigClasses() {\n" +
                    "        return new Class<?>[]{"+webconfigPackageName+"."+webConfigName+".class};\n" +
                    "    }");
            writer.println("@Override\n" +
                    "    protected String[] getServletMappings() {\n" +
                    "        return new String[]{\"/\"};\n" +
                    "    }\n" +
                    "}");
            writer.close();
        }

    }





    public static void generateWebConfig(String prefix, String packageName, String className, String controllersPackage, String viewPrefix, int controllersAmount){
        PrintWriter writer = createClassFile(prefix, packageName, className);
        if (writer != null) {
            writer.println("package " + packageName + ";");
            writer.println("import org.springframework.context.annotation.ComponentScan;\n" +
                    "import org.springframework.context.annotation.Configuration;\n" +
                    "import org.springframework.web.servlet.config.annotation.EnableWebMvc;\n" +
                    "import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;\n" +
                    "import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;");
            writer.println("@Configuration\n" +
                    "@EnableWebMvc\n"+"@ComponentScan(\""+controllersPackage+"\")");
            writer.println("public class " + className + " extends WebMvcConfigurerAdapter{\n" +
                    "                     public static final String SUFFIX = \".jsp\";\n" +
                    "                     public static final String PREFIX = \"/"+viewPrefix+"/\";");
            writer.println("@Override\n" +
                    "    public void configureViewResolvers(ViewResolverRegistry registry) {\n" +
                    "        registry.jsp(PREFIX,SUFFIX);\n" +
                    "        super.configureViewResolvers(registry);\n" +
                    "    }\n" +
                    "}");

            writer.close();
        }
        for(int i = 0; i < controllersAmount; i++){
            generateController(prefix, controllersPackage, "MyController"+i, mappingsAmount);
        }
    }
    public static void generateController(String prefix, String packageName, String controllerName, int mappingsAmount ){
        String cName = controllerName.substring(0,1).toLowerCase()+controllerName.substring(1);
        String mappingName = cName+"Path";
        PrintWriter writer = createClassFile(prefix, packageName, controllerName);
        if (writer != null) {
            writer.println("package " + packageName + ";\n"+
                    "import org.springframework.stereotype.Controller;\n"+
                    "import org.springframework.ui.Model;\n"+
                    "import org.springframework.web.bind.annotation.RequestMapping;");


            writer.println("@Controller\n"+"public class " + controllerName + " {");
            for (int i = 0; i < mappingsAmount; i++) {
                String viewName = cName + "View" + i;
                String attrName = mappingName+i+"_attr1";
                writer.println("    @RequestMapping(\"/"+mappingName+i+"\")\n"+
                        "    public String "+mappingName+i+"(Model model) {\n"+
                        "      model.addAttribute(\""+attrName+"\",\"attr1\");\n"+
                        "      return \"" + viewName + "\";\n    }");

                generateView(webroot, viewPrefix, viewName, attrName);

            }
            writer.println("}");
            writer.close();
        }

    }



    private static void generateView(String webroot, String viewPrefix, String viewName, String attrName){

        PrintWriter writer = null;
        try {
            File mypackage = new File(webroot + viewPrefix);
            mypackage.mkdir();
            writer = new PrintWriter(mypackage.getPath() + "/" + viewName + ".jsp", "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (writer != null) {
            writer.println("<%@ page contentType=\"text/html;charset=UTF-8\" language=\"java\" %>");
            writer.println("<html>");
            writer.println("<head>");
            writer.println("<title>"+viewName+"</title>");
            writer.println("</head>");
            writer.println("<body>");
            writer.println("<h1>${"+attrName+"}</h1>");
            writer.println("</body>");
            writer.println("</html>");
            writer.close();
        }
    }

    public static void generateComponents(int amount, String baseClassName, String packageName, String prefix) {
        List<String> components = new ArrayList<String>();
        for (int i = 0; i < amount; i++) {
            String newComp = generateComponent(baseClassName, packageName, prefix, components);
            boolean flag = false;
            for (String s : components) {
                if (s.equals(newComp)) {
                    flag = true;
                }
            }
            if (!flag) {
                components.add(newComp);
            }

        }
    }

    public static String generateComponent(String baseClassName, String packageName, String prefix,
                                           List<String> previous) {


        int suffix = new Random().nextInt(10000);
        String className = baseClassName + suffix;
        PrintWriter writer = createClassFile(prefix, packageName, className);
        if (writer != null) {
            writer.println("package " + packageName + ";\n" +
                    "import org.springframework.stereotype.Component;\n"+
                    "import org.springframework.beans.factory.annotation.Autowired;\n" +
                    "import org.springframework.beans.factory.annotation.Qualifier;\n\n");

            writer.println("@Component(\"" + className.toLowerCase() + "\")\n"+
                    "@Qualifier(\"" + className.toLowerCase() + "\")\n"+
                    "public class " + className + " {");

            if (previous.size() > 0) {
                for (String name : previous) {
                    writer.println("  @Autowired\n" +
                            "  @Qualifier(\"" + name.toLowerCase() + "\")\n" +
                            "  private " + name + " " + name.toLowerCase() + ";");
                }
            }
            writer.println("}");
            writer.close();
        }
        return className;
    }

    public static void generateBean(String beanName, String packageName, String prefix) {
        PrintWriter writer = createClassFile(prefix, packageName, beanName);
        if (writer != null) {
            writer.println("package " + packageName + ";\n" +
                    "public class " + beanName + " {\n" +
                    "  private String id;\n" +
                    "  public String getId() {\n" +
                    "    return id;\n  }");
            writer.println("public " + beanName + "(String id) {\n" +
                    "  this.id = id;\n  }\n }");


            writer.close();
        }
    }

    public static String createConfig(String prefix, String packageName, String className, String scannedPackages, int beansAmount, int componentsAmount){
        PrintWriter writer = createClassFile(prefix, packageName, className);
        if (writer != null) {

            writer.println("package " + packageName + ";\n"+
                    "import org.springframework.context.annotation.Configuration;\n"+
                    "import org.springframework.context.annotation.ComponentScan;\n" +
                    "import org.springframework.context.annotation.Bean;");


            writer.println("@Configuration(\"" + className.toLowerCase() + "\")\n"+
                    "@ComponentScan(\"" + scannedPackages + "\")\n"+
                    "public class " + className + " {");

            for (int i = 0; i < beansAmount; i++) {
                String beanName = className+"Bean"+i;
                generateBean(beanName,packageName,prefix);
                writer.println("@Bean(\""+beanName.toLowerCase()+"\")");

                writer.println("public " + beanName + " " + beanName.toLowerCase() + "()\n"+
                        "{\n"+
                        "return new "+beanName+"(\""+beanName.toLowerCase()+"\");\n"+
                        "}");

            }

            writer.println("}");
            writer.close();
        }

        generateComponents(componentsAmount, className+"Component",scannedPackages, prefix);
        return packageName+"."+className;
    }

    public static void generateConfigs(String prefix, String basePackage, int amount, String className){
        List<String> configs = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            String config = createConfig(prefix, basePackage+i, "Config"+i, "generatedComponents"+i, beansAmount, compsAmount);
            configs.add(config);
        }

        PrintWriter writer = createClassFile(prefix, basePackage, className);
        if (writer != null) {
            writer.println("package " + basePackage+";\n"+
                    "import org.springframework.context.annotation.Configuration;\n"+
                    "import org.springframework.context.annotation.ComponentScan;\n" +
                    "import org.springframework.context.annotation.Import;");


            writer.println("\n\n  @Configuration(\"" + className.toLowerCase() + "\")");
            writer.println("  @Import({");
            for (int i = 0; i < configs.size(); i++) {
                String s = configs.get(i);
                writer.print(s+".class");
                if(i!=amount-1){
                    writer.print(", ");
                }
            }
            writer.println("})");

            writer.println("  public class " + className + " {");

            writer.println("}");

            writer.close();
        }
    }
    private static PrintWriter createClassFile(String prefix, String packageName, String className) {
        PrintWriter writer = null;
        try {
            File mypackage = new File(prefix + packageName);
            mypackage.mkdir();
            writer = new PrintWriter(mypackage.getPath() + "/" + className + ".java", "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writer;
    }
    private static void copyTemplateFile(File source, File dest) throws IOException {
        try {
            Files.copy(source.toPath(), dest.toPath());
        } catch (FileAlreadyExistsException e){
            System.out.println("file already exists: "+e.getLocalizedMessage());
        }

    }
}