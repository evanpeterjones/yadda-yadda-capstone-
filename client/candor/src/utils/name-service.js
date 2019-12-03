/*
 * name-service utility to assign a unique username to a user-id
 */


export let animals = [
    'Giraffe','Woodpecker','Camel','Starfish','Koala','Alligator','Owl',
    'Tiger','Bear','Blue whale','Coyote','Chimpanzee','Raccoon','Lion',
    'Arctic wolf','Crocodile','Dolphin','Elephant','Squirrel','Snake',
    'Kangaroo','Hippopotamus','Elk','Fox','Gorilla','Bat','Hare','Toad',
    'Frog','Deer','Rat','Badger','Lizard','Mole','Hedgehog','Otter','Reindeer'
];

export let adjectives = [
    'Abloom', 'Abiding','Achy','Adequate','Aesthetical','Affordable','Ageless','Austere',
    'Amaranth', 'Amber', 'Apricot', 'Aqua', 'Arctic', 'Auburn', 'Almond'
];

export let comb = {/* user_id : generated name */}

function getName(user_id, uname=null, offsetAd=0, offsetAn=0) {

    if (uname) {
        comb[user_id] = uname;
    }
    
    if (comb.user_id) {
        return comb.user_id
    }

    let adj = adjectives[(user_id + offsetAd) % adjectives.length]
    let ani = animals[(user_id + offsetAn) % animals.length]
    let nam = adj + ' ' + ani;

    if (Object.values(comb).includes(nam)) {
        if (offsetAd % colors.length === user_id) {
            return getName(user_id, offsetAd, offsetAn+1)
        }
        else if (offsetAn % animals.length === user_id) {
            return getName(user_id,offsetAd + 1, offsetAn)
        } 
        else {
            delete comb.user_id
            return getName(user_id)
        }
    }
    
    return nam
};

export default getName;