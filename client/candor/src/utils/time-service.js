
function getDayAlias(time){
    var d = new Date (time);

    if (d.getDate()) {

    }
}

function getTime(time) {
    var d = new Date(time);

    var sign = (d.getHours() >= 12) ? "pm" : "am";
    var hour = (d.getHours() % 12) == 0 ? 12 : d.getHours() % 12;
    var min = (d.getMinutes() < 10) ? '0'+ d.getMinutes() : d.getMinutes();
    return hour + ":" + min + sign;
}

export default getTime;