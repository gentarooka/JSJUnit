console.log("menu test");

test("get menu", function() {
	equal(jQuery("#menu").text(), "TEST MENU",
			"We expect value to be TEST MENU");
});