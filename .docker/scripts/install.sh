#!/bin/bash

curl ${DOWNLOAD_URL} -o /opt/jboss/fuse.zip

unzip /opt/jboss/fuse.zip -d /opt/jboss && rm /opt/jboss/fuse.zip

fuse_folder=$(ls /opt/jboss)
ln -s /opt/jboss/${fuse_folder} ${FUSE_HOME}
rm -rf $FUSE_HOME/quickstarts $FUSE_HOME/docs

echo "$FUSE_USER = $FUSE_PASSWORD,admin,manager,viewer,Monitor,Operator,Maintainer,Deployer,Auditor,Administrator,SuperUser" >> $FUSE_HOME/etc/users.properties
