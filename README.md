# Modification
 1. Added pom.xml for deploy to maven
 2. Moved source to `src\main\java`
 3. Moved test to `src\test\java`
 4. Added gitignore

# Original Document
This is a pretty small set of code, folks were having trouble getting it to work correctly (to be fair I forgot a couple of files ;)) so I included an ant buildfile. It's really only got three commands:

ant compile - This will compile the files

ant example - This will run an example which will show and compare the interest points for two images of the White House

ant clean - Cleans out the build directory files

