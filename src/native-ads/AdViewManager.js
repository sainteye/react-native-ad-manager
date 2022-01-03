import React from 'react'
import { Text, View, requireNativeComponent } from 'react-native'

export const AdViewContext = React.createContext({
  register: () => {
    throw new Error('Stub!')
  },
  unregister: () => {
    throw new Error('Stub!')
  },
})

const NativeAdView = requireNativeComponent('AdView')

class AdViewChild extends React.Component {
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
    return <NativeAdView {...this.props} ref={this.handleWrapperRef} />
  }
}
export default class MediaView extends React.Component {
  render() {
    return (
      <AdViewContext.Consumer>
        {(contextValue) => <AdViewChild {...this.props} {...contextValue} />}
      </AdViewContext.Consumer>
    )
  }
}
