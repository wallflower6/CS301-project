DEPLOYMENT:
to run the file on a ec2 instance, use the following commands:
1. Run mvn clean install to get the image.

2. Copy the jar file.
scp ./<path to pem> ./<JAR snapshot file> <username>@<IP of ec2>:~

3. SSH into the instance using either the Amazon console or using ssh -i <key-pair.pem> <username>@<IP of ec2>

4. Download java jdk 11:
sudo yum install java-11-amazon-corretto

5. Run the jar file.
java -jar [path to jar file]
