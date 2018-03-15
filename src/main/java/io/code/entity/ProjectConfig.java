package io.code.entity;

import java.io.Serializable;

/**
 * project config
 *
 * @author songkejun
 * @create 2018-02-07 9:57
 **/
public class ProjectConfig implements Serializable{

    private String groupId;
    private String artifactId;
    private String fxVersion;
    private boolean rabbitMq =true;
    private String projectName;
    private String pckage ;
    private String projectType;



    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public boolean isRabbitMq() {
        return rabbitMq;
    }

    public void setRabbitMq(boolean rabbitMq) {
        this.rabbitMq = rabbitMq;
    }

    public String getFxVersion() {
        return fxVersion;
    }

    public void setFxVersion(String fxVersion) {
        this.fxVersion = fxVersion;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "ProjectConfig={groupId:"+groupId+";"+"artifactId:"+artifactId+";fxVersion:"+fxVersion+";rabbitMq:"+rabbitMq+";projectName:"+projectName+";pckage:"+pckage+";projectType:"+projectType+"}";
    }

    public String getPckage() {
        return pckage;
    }

    public void setPckage(String pckage) {
        this.pckage = pckage;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }
}
