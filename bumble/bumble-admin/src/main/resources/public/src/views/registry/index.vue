<template>
  <div class="app-container xxx">
    <div class="editor-container">
      <json-editor ref="jsonEditor" v-model="value"></json-editor>
    </div>
  </div>
</template>

<script>
import JsonEditor from '@/components/JsonEditor'
import { fetchAll } from '@/api/registry'

export default {
  components: { JsonEditor },
  data() {
    return {
      value: null
    }
  },
  filters: {
    statusFilter(status) {
      const statusMap = {
        success: 'success',
        pending: 'danger'
      }
      return statusMap[status]
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      fetchAll().then(response => {
        this.value = response.data
      })
    }
  }
}
</script>

