// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: DeviceName.proto

package org.mixedsignals.resignal.devicelist;

public final class DeviceNameProtos {
  private DeviceNameProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface DeviceNameOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    // optional bytes ephemeralPublic = 1;
    /**
     * <code>optional bytes ephemeralPublic = 1;</code>
     */
    boolean hasEphemeralPublic();
    /**
     * <code>optional bytes ephemeralPublic = 1;</code>
     */
    com.google.protobuf.ByteString getEphemeralPublic();

    // optional bytes syntheticIv = 2;
    /**
     * <code>optional bytes syntheticIv = 2;</code>
     */
    boolean hasSyntheticIv();
    /**
     * <code>optional bytes syntheticIv = 2;</code>
     */
    com.google.protobuf.ByteString getSyntheticIv();

    // optional bytes ciphertext = 3;
    /**
     * <code>optional bytes ciphertext = 3;</code>
     */
    boolean hasCiphertext();
    /**
     * <code>optional bytes ciphertext = 3;</code>
     */
    com.google.protobuf.ByteString getCiphertext();
  }
  /**
   * Protobuf type {@code signalservice.DeviceName}
   */
  public static final class DeviceName extends
      com.google.protobuf.GeneratedMessage
      implements DeviceNameOrBuilder {
    // Use DeviceName.newBuilder() to construct.
    private DeviceName(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private DeviceName(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final DeviceName defaultInstance;
    public static DeviceName getDefaultInstance() {
      return defaultInstance;
    }

    public DeviceName getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private DeviceName(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              bitField0_ |= 0x00000001;
              ephemeralPublic_ = input.readBytes();
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              syntheticIv_ = input.readBytes();
              break;
            }
            case 26: {
              bitField0_ |= 0x00000004;
              ciphertext_ = input.readBytes();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.mixedsignals.resignal.devicelist.DeviceNameProtos.internal_static_signalservice_DeviceName_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.mixedsignals.resignal.devicelist.DeviceNameProtos.internal_static_signalservice_DeviceName_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceName.class, org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceName.Builder.class);
    }

    public static com.google.protobuf.Parser<DeviceName> PARSER =
        new com.google.protobuf.AbstractParser<DeviceName>() {
      public DeviceName parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new DeviceName(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<DeviceName> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    // optional bytes ephemeralPublic = 1;
    public static final int EPHEMERALPUBLIC_FIELD_NUMBER = 1;
    private com.google.protobuf.ByteString ephemeralPublic_;
    /**
     * <code>optional bytes ephemeralPublic = 1;</code>
     */
    public boolean hasEphemeralPublic() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional bytes ephemeralPublic = 1;</code>
     */
    public com.google.protobuf.ByteString getEphemeralPublic() {
      return ephemeralPublic_;
    }

    // optional bytes syntheticIv = 2;
    public static final int SYNTHETICIV_FIELD_NUMBER = 2;
    private com.google.protobuf.ByteString syntheticIv_;
    /**
     * <code>optional bytes syntheticIv = 2;</code>
     */
    public boolean hasSyntheticIv() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional bytes syntheticIv = 2;</code>
     */
    public com.google.protobuf.ByteString getSyntheticIv() {
      return syntheticIv_;
    }

    // optional bytes ciphertext = 3;
    public static final int CIPHERTEXT_FIELD_NUMBER = 3;
    private com.google.protobuf.ByteString ciphertext_;
    /**
     * <code>optional bytes ciphertext = 3;</code>
     */
    public boolean hasCiphertext() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional bytes ciphertext = 3;</code>
     */
    public com.google.protobuf.ByteString getCiphertext() {
      return ciphertext_;
    }

    private void initFields() {
      ephemeralPublic_ = com.google.protobuf.ByteString.EMPTY;
      syntheticIv_ = com.google.protobuf.ByteString.EMPTY;
      ciphertext_ = com.google.protobuf.ByteString.EMPTY;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, ephemeralPublic_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, syntheticIv_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBytes(3, ciphertext_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, ephemeralPublic_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, syntheticIv_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, ciphertext_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceName parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceName parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceName parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceName parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceName parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceName parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceName parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceName parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceName parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceName parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceName prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code signalservice.DeviceName}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceNameOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return org.mixedsignals.resignal.devicelist.DeviceNameProtos.internal_static_signalservice_DeviceName_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return org.mixedsignals.resignal.devicelist.DeviceNameProtos.internal_static_signalservice_DeviceName_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceName.class, org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceName.Builder.class);
      }

      // Construct using org.thoughtcrime.securesms.devicelist.DeviceNameProtos.DeviceName.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        ephemeralPublic_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000001);
        syntheticIv_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000002);
        ciphertext_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return org.mixedsignals.resignal.devicelist.DeviceNameProtos.internal_static_signalservice_DeviceName_descriptor;
      }

      public org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceName getDefaultInstanceForType() {
        return org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceName.getDefaultInstance();
      }

      public org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceName build() {
        org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceName result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceName buildPartial() {
        org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceName result = new org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceName(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.ephemeralPublic_ = ephemeralPublic_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.syntheticIv_ = syntheticIv_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.ciphertext_ = ciphertext_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceName) {
          return mergeFrom((org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceName)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceName other) {
        if (other == org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceName.getDefaultInstance()) return this;
        if (other.hasEphemeralPublic()) {
          setEphemeralPublic(other.getEphemeralPublic());
        }
        if (other.hasSyntheticIv()) {
          setSyntheticIv(other.getSyntheticIv());
        }
        if (other.hasCiphertext()) {
          setCiphertext(other.getCiphertext());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceName parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (org.mixedsignals.resignal.devicelist.DeviceNameProtos.DeviceName) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      // optional bytes ephemeralPublic = 1;
      private com.google.protobuf.ByteString ephemeralPublic_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>optional bytes ephemeralPublic = 1;</code>
       */
      public boolean hasEphemeralPublic() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional bytes ephemeralPublic = 1;</code>
       */
      public com.google.protobuf.ByteString getEphemeralPublic() {
        return ephemeralPublic_;
      }
      /**
       * <code>optional bytes ephemeralPublic = 1;</code>
       */
      public Builder setEphemeralPublic(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        ephemeralPublic_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional bytes ephemeralPublic = 1;</code>
       */
      public Builder clearEphemeralPublic() {
        bitField0_ = (bitField0_ & ~0x00000001);
        ephemeralPublic_ = getDefaultInstance().getEphemeralPublic();
        onChanged();
        return this;
      }

      // optional bytes syntheticIv = 2;
      private com.google.protobuf.ByteString syntheticIv_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>optional bytes syntheticIv = 2;</code>
       */
      public boolean hasSyntheticIv() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional bytes syntheticIv = 2;</code>
       */
      public com.google.protobuf.ByteString getSyntheticIv() {
        return syntheticIv_;
      }
      /**
       * <code>optional bytes syntheticIv = 2;</code>
       */
      public Builder setSyntheticIv(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        syntheticIv_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional bytes syntheticIv = 2;</code>
       */
      public Builder clearSyntheticIv() {
        bitField0_ = (bitField0_ & ~0x00000002);
        syntheticIv_ = getDefaultInstance().getSyntheticIv();
        onChanged();
        return this;
      }

      // optional bytes ciphertext = 3;
      private com.google.protobuf.ByteString ciphertext_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>optional bytes ciphertext = 3;</code>
       */
      public boolean hasCiphertext() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>optional bytes ciphertext = 3;</code>
       */
      public com.google.protobuf.ByteString getCiphertext() {
        return ciphertext_;
      }
      /**
       * <code>optional bytes ciphertext = 3;</code>
       */
      public Builder setCiphertext(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        ciphertext_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional bytes ciphertext = 3;</code>
       */
      public Builder clearCiphertext() {
        bitField0_ = (bitField0_ & ~0x00000004);
        ciphertext_ = getDefaultInstance().getCiphertext();
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:signalservice.DeviceName)
    }

    static {
      defaultInstance = new DeviceName(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:signalservice.DeviceName)
  }

  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_signalservice_DeviceName_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_signalservice_DeviceName_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\020DeviceName.proto\022\rsignalservice\"N\n\nDev" +
      "iceName\022\027\n\017ephemeralPublic\030\001 \001(\014\022\023\n\013synt" +
      "heticIv\030\002 \001(\014\022\022\n\nciphertext\030\003 \001(\014B9\n%org" +
      ".thoughtcrime.securesms.devicelistB\020Devi" +
      "ceNameProtos"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_signalservice_DeviceName_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_signalservice_DeviceName_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_signalservice_DeviceName_descriptor,
              new java.lang.String[] { "EphemeralPublic", "SyntheticIv", "Ciphertext", });
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }

  // @@protoc_insertion_point(outer_class_scope)
}
