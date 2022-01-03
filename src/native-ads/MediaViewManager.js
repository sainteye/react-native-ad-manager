import React from 'react'
import { Text, View, requireNativeComponent } from 'react-native'

export const MediaViewContext = React.createContext({
  register: () => {
    throw new Error('Stub!')
  },
  unregister: () => {
    throw new Error('Stub!')
  },
})

const NativeMediaView = requireNativeComponent('MediaView')

class MediaViewChild extends React.Component {
  constructor() {
    super(...arguments)
    this.wrapperRef = null
    this.handleWrapperRef = (ref) => {
      if (this.wrapperRef) {
        this.props.unregister(this.wrapperRef)
        this.wrapperRef = null
      }
      if (ref) {
        this.props.register(ref)
        this.wrapperRef = ref
      }
    }
  }
  render() {
    return <NativeMediaView {...this.props} ref={this.handleWrapperRef} />
  }
}
export default class MediaView extends React.Component {
  render() {
    return (
      <MediaViewContext.Consumer>
        {(contextValue) => <MediaViewChild {...this.props} {...contextValue} />}
      </MediaViewContext.Consumer>
    )
  }
}
