#!/bin/sh

MockGoogleServices='{"project_info":{"project_number":"1","project_id":"example","storage_bucket":"example.appspot.com"},"client":[{"client_info":{"mobilesdk_app_id":"id","android_client_info":{"package_name":"movie.metropolis.app"}},"oauth_client":[{"client_id":"id.apps.googleusercontent.com","client_type":1,"android_info":{"package_name":"movie.metropolis.app","certificate_hash":"hash"}},{"client_id":"id.apps.googleusercontent.com","client_type":3}],"api_key":[{"current_key":"key"}],"services":{"appinvite_service":{"other_platform_oauth_client":[{"client_id":"id.apps.googleusercontent.com","client_type":3}]}}},{"client_info":{"mobilesdk_app_id":"id","android_client_info":{"package_name":"movie.metropolis.app.dev"}},"oauth_client":[{"client_id":"id.apps.googleusercontent.com","client_type":1,"android_info":{"package_name":"movie.metropolis.app.dev","certificate_hash":"id"}},{"client_id":"id.apps.googleusercontent.com","client_type":3}],"api_key":[{"current_key":"key"}],"services":{"appinvite_service":{"other_platform_oauth_client":[{"client_id":"id.apps.googleusercontent.com","client_type":3}]}}}],"configuration_version":"1"}'

echo $MockGoogleServices > app/google-services.json
echo $MockGoogleServices > app-wear/google-services.json
echo "MM_BASIC_USER=user" >> secrets.properties
echo "MM_BASIC_PASS=pass" >> secrets.properties
echo "MM_CAPTCHA=captcha" >> secrets.properties
echo "MM_TMDB=tmdb" >> secrets.properties