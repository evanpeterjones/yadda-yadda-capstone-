import moment from 'moment';

const time = {
    getTimeSince(time, mobile) {
        var res = moment(time).fromNow();
        return res;
    },
    formatTime(time) {
        return moment(new Date(time)).format('YYYY-MM-DD')
    }
};

export default time;