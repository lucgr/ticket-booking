#! /bin/bash

EFS="fs-03c0100189d67a1e2.efs.eu-central-1.amazonaws.com"
IMAGE="public.ecr.aws/n3t0u8t2/conductor-oss:3.21.12"
CONFIG_PATH="/home/ubuntu/app/config"
CONFIG_FILE="aws-config.properties"

sudo apt upgrade -y

sudo snap install docker

sudo apt install nfs-common -y

sudo mkdir -p $CONFIG_PATH

sudo echo "$EFS:/ $CONFIG_PATH nfs4 nfsvers=4.1,rsize=1048576,wsize=1048576,hard,timeo=600,retrans=2,noresvport,_netdev,nofail 0 0" | sudo tee -a /etc/fstab

sudo systemctl daemon-reload

sudo mount -a

sudo docker pull $IMAGE

sudo docker run \
-v $CONFIG_PATH:/app/config \
--env CONFIG_PROP=$CONFIG_FILE \
-p 8080:8080 -p 5000:5000 \
--name conductor \
--restart=always \
$IMAGE