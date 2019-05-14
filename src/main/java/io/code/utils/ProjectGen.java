package io.code.utils;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * generte a project
 *
 * @author songkejun
 * @create 2018-02-07 9:50
 **/
public class ProjectGen {

    private static ProjectGen gen = null;

    public static ProjectGen getInstance() {
        if (gen == null) {
            gen = new ProjectGen();
        }
        return gen;
    }


    public byte[] generate() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        Configuration config = GenUtils.getConfig();
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("desc", "code-generator desc2");
        paraMap.put("artifactId", "code-generator");
        paraMap.put("package", config.getString("package"));

        VelocityContext velocityCtx = new VelocityContext(paraMap);
        try {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate("template/pom.xml.vm", "UTF-8");
            tpl.merge(velocityCtx, sw);
            sw.flush();
            // main project
            String mainProject = Constant.PROJECTNAME;
            String mainFold = mainProject + File.separator + "pom.xml";
            zip.putNextEntry(new ZipEntry(mainFold));
            IOUtils.write(sw.toString(), zip, "UTF-8");
            IOUtils.closeQuietly(sw);
            sw.flush();
            zip.closeEntry();
            //core generate
            String core = mainProject + File.separator + Constant.PROJECTCORE + File.separator + "pom.xml";
            zip.putNextEntry(new ZipEntry(core));
            IOUtils.write(sw.toString(), zip, "UTF-8");
            IOUtils.closeQuietly(sw);
            zip.closeEntry();
            // generate controller
            StringWriter consw = new StringWriter();
            Template controllerTempl = Velocity.getTemplate("template/Controller.java.vm", "UTF-8");
            controllerTempl.merge(velocityCtx, consw);
            zip.putNextEntry(new ZipEntry(getFileName(Constant.PROJECTCORE, "com.ddd.core", "ql-template")));
            IOUtils.write(consw.toString(), zip, "UTF-8");

            addResources(zip,velocityCtx);

            IOUtils.closeQuietly(consw);
            zip.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }
        IOUtils.closeQuietly(zip);

        return outputStream.toByteArray();
    }

    private String getFileName(String module, String pckage, String projectName) {
        pckage = pckage.replace(".", File.separator);
        String mainName = projectName + File.separator + module + File.separator + "src" + File.separator + "main"+File.separator +"java"+ File.separator + pckage + File.separator + "Controller.java";
        return mainName;
    }

    private void addResources(ZipOutputStream zip, VelocityContext velocityCtx) {

        String mainRes = Constant.PROJECTNAME+File.separator +Constant.PROJECTCORE + File.separator + "src" + File.separator + "main" + File.separator + "resources";

        try {
            String mdProp = mainRes + File.separator + "dd.properties";
            generateFile(zip,velocityCtx,"template/resources/dd.properties.vm",ddd);

            //app
            String appxml = mainRes + File.separator + "template-sample-application.xml";
            generateFile(zip,velocityCtx,"template/resources/template-sample-application.xml.vm",appxml);
            //svlet
            String svletxml = mainRes + File.separator + "template-sample-servlet.xml";
            generateFile(zip,velocityCtx,"template/resources/template-sample-servlet.xml.vm",svletxml);

            //metinfo
            String metxml = mainRes + File.separator + "META-INF"+File.separator+"app.properties";
            generateFile(zip,velocityCtx,"template/resources/meta-info-app.properties.vm",metxml);
            //configs
            String config = mainRes + File.separator + "configs"+File.separator+"server.properties";
            generateFile(zip,velocityCtx,"template/resources/configs-server.properties.vm",config);
            //springs
            String sp = mainRes + File.separator + "springs"+File.separator+"rabbitmq-sample-consumer.xml";
            generateFile(zip,velocityCtx,"template/resources/springs-rabbitmq-consumer.xml.vm",sp);


            String springPrd = mainRes + File.separator + "springs"+File.separator+"rabbitmq-sample-producer.xml";
            generateFile(zip,velocityCtx,"template/resources/springs-rabbitmq-producer.xml.vm",springPrd);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateFile(ZipOutputStream zip, VelocityContext velocityCtx,String template,String path){
        try{
            StringWriter prdsw = new StringWriter();
            Template spdTemp = Velocity.getTemplate(template, "UTF-8");
            spdTemp.merge(velocityCtx, prdsw);
            zip.putNextEntry(new ZipEntry(path));
            IOUtils.write(prdsw.toString(), zip, "UTF-8");
            IOUtils.closeQuietly(prdsw);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
