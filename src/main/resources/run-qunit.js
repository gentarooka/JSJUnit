/**
 * Wait until the test condition is true or a timeout occurs. Useful for waiting
 * on a server response or for a ui change (fadeIn, etc.) to occur.
 *
 * @param testFx javascript condition that evaluates to a boolean,
 * it can be passed in as a string (e.g.: "1 == 1" or "$('#bar').is(':visible')" or
 * as a callback function.
 * @param onReady what to do when testFx condition is fulfilled,
 * it can be passed in as a string (e.g.: "1 == 1" or "$('#bar').is(':visible')" or
 * as a callback function.
 * @param timeOutMillis the max amount of time to wait. If not specified, 3 sec is used.
 */
function waitFor(testFx, onReady, timeOutMillis) {
    var maxtimeOutMillis = timeOutMillis ? timeOutMillis : 3001, //< Default Max Timout is 3s
        start = new Date().getTime(),
        condition = false,
        interval = setInterval(function() {
            if ( (new Date().getTime() - start < maxtimeOutMillis) && !condition ) {
                // If not time-out yet and condition not yet fulfilled
                condition = (typeof(testFx) === "string" ? eval(testFx) : testFx()); //< defensive code
            } else {
                if(!condition) {
                    // If condition still not fulfilled (timeout but condition is 'false')
                    console.log("'waitFor()' timeout");
                    phantom.exit(1);
                } else {
                    // Condition fulfilled (timeout and/or condition is 'true')
                    console.log("'waitFor()' finished in " + (new Date().getTime() - start) + "ms.");
                    typeof(onReady) === "string" ? eval(onReady) : onReady(); //< Do what it's supposed to do once the condition is fulfilled
                    clearInterval(interval); //< Stop this interval
                }
            }
        }, 100); //< repeat check every 250ms
};


if (phantom.args.length === 0) {
    console.log('Usage: run-qunit.js URL TEST_SCRIPTS...');
    phantom.exit();
}


var url = phantom.args[0]; 
var js = new Array();
for (var i = 1; i < phantom.args.length;i++) {
	js.push(phantom.args[i]);
}

openUrl(url, js);

function openUrl(testUrl, jsArray) {
	
	var page = new WebPage();

	// Route "console.log()" calls from within the Page context to the main Phantom context (i.e. current "this")
	page.onConsoleMessage = function(msg) {
		console.log(msg);
	};
	
	page.open(testUrl, function(status) {
		if (status !== "success") {
			console.log("Unable to access network");
			phantom.exit();
		} else {
			bindQunit(page, testUrl);
			
			var isLoaded = false;
			for (var i = 0; i< jsArray.length;i++) {
				isLoaded = page.injectJs(jsArray[i]) || isLoaded;
			}

			if (!isLoaded) {
				page.evaluate(function() {
					test("test case does not exist", function() {
						equal(true, false, "test case does not exist");
					});
				});
			}

			page.evaluate(function() {
				QUnit.load();
			});

			waitFor(function() {
				return page.evaluate(function() {
					if (typeof (runQunitFinished) == "undefined"
							|| !runQunitFinished) {
						return false;
					}
					;

					return true;
				});
			}, function() {
				phantom.exit();
//				if (testUrls.length === 0) {
//					phantom.exit();
//				}
//				openUrl(testUrls);
			}, 10000);
		}
	});

}

function bindQunit(page, testUrl) {
	page.injectJs("./qunit.js");

	page.evaluate("function() {testUrl = \"" + testUrl + "\";}");

	page.evaluate(function() {
		runQunitFinished = false;

		var children = new Array();
		var details = new Array();
		QUnit.testDone(function(data) {
			data.details = details;
			children.push(data);
		});

		QUnit.done(function(data) {
			data.children = children;
			
			var xhr = new XMLHttpRequest();
			xhr.open("POST", "/jsjunit/result", false);
			xhr.setRequestHeader("Content-Type", "application/json");
			try {
				xhr.send(JSON.stringify(data));
			} catch (e) {
				// Just swallow exceptions as we can't do anything useful if
				// there are
				// comms errors.
			}
			
			runQunitFinished = true;
		});
		
		QUnit.log(function(data){
			details.push(data);
		});
	});

}

