

Constraints for a Valid Password:
<br>It must contain at least 8 characters and at most 20 characters.
<br>It must have at least one digit.
<br>It must have at least one uppercase alphabet.
<br>It must have at least one lowercase alphabet.
<br>It must have at least one special character (such as !@#$%&*()-+=^).
<br>It should not contain any white spaces.


Dockerise the SpringBoot Application with MYSQL

mysql Port: 3306<br>
SpringBoot Port: 8089

SpringBoot Application:
<ul>
      <li>Toggle the Thunder button in Maven Tab.</li>
      <li>Click clean  and then install in Lifecycle</li>
    <li> Jarfile will be created.
</ul>

![img.png](src/main/resources/image/Maven.png)
<ul>
<li>Open the Terminal in the root level of the project</li>
<li>docker compose -f .\docker-compose.yaml up</li>
<li>It will run both the image. </li>
<li>Open the Docker Desktop and pause the backend image in the container</li>
<li>After the postgresql image created , start the backend image</li>


![img.png](src/main/resources/image/Maven_1.png)