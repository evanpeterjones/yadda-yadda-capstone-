import moment from 'moment';

function getTimeSince(time, mobile) {
    var res = moment(time).fromNow();
    return res;
}

export default getTimeSince;