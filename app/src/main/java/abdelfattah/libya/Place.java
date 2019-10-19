package abdelfattah.libya;

public class Place {
    private int mImageResourceId;
    private int mNameStringId;
    private int mAddressStringId;
    private int mAudioResourceId;

    Place(int mImageResourceId, int mNameStringId, int mAddressStringId, int mAudioResourceId) {
        this.mImageResourceId = mImageResourceId;
        this.mNameStringId = mNameStringId;
        this.mAddressStringId = mAddressStringId;
        this.mAudioResourceId = mAudioResourceId;
    }

    int getImageResourceId() {
        return mImageResourceId;
    }

    int getNameStringId() {
        return mNameStringId;
    }

    int getAddressStringId() {
        return mAddressStringId;
    }

    int getAudioResourceId() {
        return mAudioResourceId;
    }
}