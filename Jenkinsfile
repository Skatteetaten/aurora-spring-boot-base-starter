#!/usr/bin/env groovy
fileLoader.withGit(config.pipelineScript, config.scriptVersion) {
   jenkinsfile = fileLoader.load('templates/leveransepakke')
}

def config = [
        disableAllReports: true,
        credentialsId: "github",
        deployTo: 'maven-central',
        openShiftBuild: false,
        scriptVersion : 'v7',
        iqOrganizationName: "Team AOS",
        compilePropertiesIq: "-x test",
        javaVersion : '11',
        jiraFiksetIKomponentversjon: true,
        chatRoom: "#aos-notifications",
        pipelineScript : 'https://git.aurora.skead.no/scm/ao/aurora-pipeline-scripts.git',
        versionStrategy : [
                [branch: 'master', versionHint: '1']
        ]
]

CENTRAL=true

jenkinsfile.gradle(config.scriptVersion, config)