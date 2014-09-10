<?php
	header('Content-Type: application/json; charset=utf-8');

	$lastUpdated = $_POST["last_updated"];

	if( empty($lastUpdated) ) {

		define("BASE_URL", "http://talkie-kids.co/app/");
		define("RESOURCES_PATH", "resources/");
		define("AUDIO_PATH", "audio/");
		//define("RESOURCES_PATH", "resources/");
		define("JSON_WORDS_NOSPECIFIC", "words.json");
		define("JSON_LANGUAGES", "languages.json");
		define("JSON_CATEGORIES", "categories.json");
		define("JSON_REF_WORDS_LANG", "ref_words_languages.json");

		define("LDPI", "ldpi");
		define("MDPI", "mdpi");
		define("TVDPI", "tvdpi");
		define("HDPI", "hdpi");
		define("XHDPI", "xhdpi");
		define("XXHDPI", "xxhdpi");
		define("XXXHDPI", "xxxhdpi");

		define("NORMAL", "normal");
		define("LARGE", "large");
		define("XLARGE", "xlarge");

		define("NORMAL_LDPI", "320x240/");
		define("NORMAL_MDPI", "480x320/");
		define("LARGE_MDPI", "1024x600/");
		define("XLARGE_MDPI", "1280x800/");
		define("LARGE_TVDPI", "1280x800/");
		define("NORMAL_HDPI", "800x480/");
		define("NORMAL_XHDPI", "1280x800/");
		define("NORMAL_XXHDPI", "1920x1080/");

		$deviceScreenSize = $_POST["screen_size"];
		$deviceDensity = $_POST["density"];
		$deviceWidth = $_POST["width"];
		$deviceHeight = $_POST["height"];
		$deviceId = $_POST["device_id"];

		if( empty( $deviceScreenSize ) ) {
			$deviceScreenSize = $_GET["screen_size"];
		}

		if( empty( $deviceDensity ) ) {
			$deviceDensity = $_GET["density"];
		}

		$isResolutionUnknown = false;
		$screenSize = null;
		$density = null;

		if ( strcasecmp($deviceScreenSize, LARGE) == 0 ) {
			$screenSize = LARGE;
		} elseif ( strcasecmp($deviceScreenSize, XLARGE) == 0 ) {
			$screenSize = XLARGE;
		} elseif ( strcasecmp($deviceScreenSize, NORMAL) == 0 ) {
			$screenSize = NORMAL;
		} else {
			$isResolutionUnknown = true;
		}
			
		if( strcasecmp($deviceDensity, MDPI) == 0 ) {
			$density = MDPI;
		} elseif ( strcasecmp($deviceDensity, TVDPI) == 0 ) {
			$density = TVDPI;
		} elseif ( strcasecmp($deviceDensity, LDPI) == 0 ) {
			$density = LDPI;
		} elseif ( strcasecmp($deviceDensity, HDPI) == 0 ) {
			$density = HDPI;
		} elseif ( strcasecmp($deviceDensity, XHDPI) == 0 ) {
			$density = XHDPI;
		} elseif ( strcasecmp($deviceDensity, XXHDPI) == 0 ) {
			$density = XXHDPI;
		} elseif ( strcasecmp($deviceDensity, XXXHDPI) == 0 ) {
			$density = XXXHDPI;

			//TODO: remove this line when there are available resources for xxxhdpi
			$isResolutionUnknown = true;
		} else {
			$isResolutionUnknown = true;
		}

		$deviceSpecificPath = null;

		if( !$isResolutionUnknown ) {

			if ( strcasecmp($deviceScreenSize, NORMAL) == 0 ) {
				if( strcasecmp($deviceDensity, LDPI) == 0 ) {
					$deviceSpecificPath = NORMAL_LDPI;
				} elseif ( strcasecmp($deviceDensity, MDPI) == 0 ) {
					$deviceSpecificPath = NORMAL_MDPI;
				} elseif ( strcasecmp($deviceDensity, HDPI) == 0 ) {
					$deviceSpecificPath = NORMAL_HDPI;
				} elseif ( strcasecmp($deviceDensity, XHDPI) == 0 ) {
					$deviceSpecificPath = NORMAL_XHDPI;
				} elseif ( strcasecmp($deviceDensity, XXHDPI) == 0 ) {
					$deviceSpecificPath = NORMAL_XXHDPI;
				} else {
					$isResolutionUnknown = true;
				}
			} elseif ( strcasecmp($deviceScreenSize, LARGE) == 0 ) {
				if( strcasecmp($deviceDensity, MDPI) == 0 ) {
					$deviceSpecificPath = LARGE_MDPI;
				} elseif ( strcasecmp($deviceDensity, TVDPI) == 0 ) {
					$deviceSpecificPath = LARGE_TVDPI;
				} else {
					$isResolutionUnknown = true;
				}
			} elseif ( strcasecmp($deviceScreenSize, XLARGE) == 0 ) {
				if( strcasecmp($deviceDensity, MDPI) == 0 ) {
					$deviceSpecificPath = XLARGE_MDPI;
				} else {
					$isResolutionUnknown = true;
				}
			} else {
				$isResolutionUnknown = true;
			}
		}

		if($isResolutionUnknown) {
			//TODO: calculate device resolution and choose right resources
			$deviceSpecificPath = NORMAL_MDPI;
		}

		$resourcesPath = BASE_URL.RESOURCES_PATH;

		$response["is_update_needed"] = true;
		$response["url_resources"] = $resourcesPath.$deviceSpecificPath;
		$response["url_audio"] = BASE_URL.AUDIO_PATH;
		$response["words"] = json_decode(file_get_contents($resourcesPath.JSON_WORDS_NOSPECIFIC));
		$response["categories"] = json_decode(file_get_contents($resourcesPath.JSON_CATEGORIES));
		$response["languages"] = json_decode(file_get_contents($resourcesPath.JSON_LANGUAGES));
		$response["ref_words_languages"] = json_decode(file_get_contents($resourcesPath.JSON_REF_WORDS_LANG));
		$response["last_updated"] = date("Y-m-d");
		
		/* FOR TEST CASES
		echo "deviceSpecificPath: ";
		echo $deviceSpecificPath;
		echo "<br />isResolutionUnknown: ";
		echo $isResolutionUnknown;
		echo "<br />density: ";
		echo $density;
		echo "<br />screenSize: ";
		echo $screenSize;
		echo "<br />deviceDensity: ";
		echo $deviceDensity;
		echo "<br />deviceScreenSize: ";
		echo $deviceScreenSize;
		*/
	} else {
		$response["is_update_needed"] = false;
	}
	
	echo json_encode($response);