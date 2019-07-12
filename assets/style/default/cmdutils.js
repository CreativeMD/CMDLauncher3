function sendNotification(control, event, data) {
  window.screenControl({
    request: control + ":" + event + ":" + data,
    onSuccess: function(response) {
      alert("Notification has been received: " + response);
    },
    onFailure: function(errCode, errMsg) {
      alert("Notification has been canceled: " + errMsg + ", " + errCode);
    }
  });
}