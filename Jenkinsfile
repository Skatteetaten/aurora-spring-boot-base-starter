#!/usr/bin/env groovy

def config = [
    scriptVersion  : 'v7',
    credentialsId: 'github',
    javaVersion: "11",
    docs: false,
    sonarQube: false,
    openShiftBuild: false,
    manualReleaseEnabled: true,
    versionStrategy: [],
    iqOrganizationName: "Team AOS",
    chatRoom: "#aos-notifications"
]

fileLoader.withGit('https://git.aurora.skead.no/scm/ao/aurora-pipeline-scripts.git', version) {
    jenkinsfile = fileLoader.load('templates/leveransepakke')
}

jenkinsfile.gradle(config.scriptVersion, config)
