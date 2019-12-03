// Copyright 2018 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

'use strict';

chrome.runtime.onInstalled.addListener(function() {
  navigator.geolocation.getCurrentPosition(function(res) { 
    chrome.tabs.query({'active': true, 'lastFocusedWindow': true}, function(tabs) {

      let request = new XMLHttpRequest();
      let params = null;

      if (res.coords) {
        params = {
          'lat' : res.coords.latitude,
          'long' : res.coords.longitude,
          'link' : 'http://google.com',
          'content' : 'test post'
        };
      }

      console.log(JSON.stringify(params));

      request.addEventListener("load", function (response) {
        console.log(response.data)
      });

      request.open("GET", "http://localhost:5000/newPost", true);
//      request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
      request.send(JSON.stringify(params));

      console.log("finished posting");

    });
  });

  chrome.declarativeContent.onPageChanged.removeRules(undefined, function() {
    chrome.declarativeContent.onPageChanged.addRules([{
      conditions: [new chrome.declarativeContent.PageStateMatcher({
        pageUrl: {hostEquals: 'https://www.internetizens.net'}
      })],
      actions: [new chrome.declarativeContent.ShowPageAction()]
    }]);
  });
});
