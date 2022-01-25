<template>
  <a-layout-footer style="text-align: center">
    <span v-show="user.id">Welcome {{user.name}}<br></span>

    Wiki ©2022 Created by Zibo Wang
  </a-layout-footer>
</template>

<script lang="ts">
import {computed, defineComponent} from 'vue';
import store from "@/store";

export default defineComponent({
  name: 'the-footer',
  props: {
    msg: String,
  },
  setup(){
    const user = computed(() => store.state.user);
    return {
      user
    }
  }
});
</script>
