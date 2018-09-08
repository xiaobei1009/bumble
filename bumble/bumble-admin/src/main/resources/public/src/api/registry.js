import request from '@/utils/request'

export function fetchAll(query) {
  return request({
    url: '/registry/all',
    method: 'get',
    params: query
  })
}
