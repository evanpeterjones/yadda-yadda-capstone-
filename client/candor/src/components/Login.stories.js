import { storiesOf } from "@storybook/vue";
//import * from '../stories/index'
import { Login } from './Login.vue';

storiesOf('Login', module)
    .add('test', () => ({
        template: '<button>Displaying</button>'
    }))
    .add('dialog', () => ({
        components: [Login],
        template: '<login></login>'
    }));