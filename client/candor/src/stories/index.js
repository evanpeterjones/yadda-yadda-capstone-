import storiesOf from '@storybook/vue';
import Login from '../components/Login.vue';

export default { title: 'Login' };

export const test = () => '<button> Test Button </button>'

export const login = () => '<Login />'

export const withText = () => ({
    render: h => "<Login/>"
});

export const asAComponent = () => ({
    name: 'component test',
    components: { Login },
    template: '<Login />'
});

// global stories go here 
// this project doesn't need every component to generate its own stories
storiesOf('Login', module)
    .add('test', () => ({
        components: Login,
        template: '<Login/>'
    }));