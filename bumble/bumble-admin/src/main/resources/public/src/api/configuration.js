import request from '@/utils/request'

export function fetchAll(query) {
  return request({
    url: '/config/all',
    method: 'get',
    params: query
  })
}
