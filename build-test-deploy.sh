
#!/bin/bash
set -ev
if [ $TRAVIS_TAG ] && [[ $TRAVIS_TAG == release* ]] ; then
  echo "Assembling and publishing prod release"
  ./gradlew clean assembleProdRelease assembleDevRelease assembleMockRelease publishApkProdRelease
  #tar -zcvf mappings.tar.gz --exclude='dump.*' app/build/outputs/mapping
  #openssl aes-256-cbc -k $VE_PROD_KEY_PASS -in mappings.tar.gz -out mappings.tar.gz.enc
elif [ $TRAVIS_TAG ] ; then
  echo "Assembling and uploading release to GitHub"
  ./gradlew clean assembleProdRelease assembleDevRelease assembleMockRelease
  #tar -zcvf mappings.tar.gz --exclude='dump.*' app/build/outputs/mapping
  #openssl aes-256-cbc -k $VE_DEV_KEY_PASS -in mappings.tar.gz -out mappings.tar.gz.enc
else
  echo "Assembling and testing Debug"
  ./gradlew clean assembleProdDebugUnitTest testProdDebug --tests="com.alimuzaffar.blank.*"
fi
