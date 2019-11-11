
export let animals = [
    'Giraffe','Woodpecker','Camel','Starfish','Koala','Alligator','Owl',
    'Tiger','Bear','Blue whale','Coyote','Chimpanzee','Raccoon','Lion',
    'Arctic wolf','Crocodile','Dolphin','Elephant','Squirrel','Snake',
    'Kangaroo','Hippopotamus','Elk','Fox','Gorilla','Bat','Hare','Toad',
    'Frog','Deer','Rat','Badger','Lizard','Mole','Hedgehog','Otter','Reindeer'
];

export let colors = [    
    'Amaranth', 'Amber', 'Apricot', 'Aqua', 'Arctic', 'Auburn', 'Almond'
];

export let adjectives = [
    'Abloom', 'Abiding','Achy','Adequate','Aesthetical','Affordable','Ageless','Austere'
];

function getName(user_id) {
    return adjectives[user_id % colors.length] + ' ' + animals[user_id % animals.length]
};

export default getName;