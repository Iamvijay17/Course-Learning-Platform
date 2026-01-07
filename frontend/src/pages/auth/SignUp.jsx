import { FaUser, FaPhoneAlt, FaEnvelope, FaFacebookF, FaInstagram, FaTwitter } from "react-icons/fa";

export default function SignUp() {
  return (
    <div className="min-h-screen flex">
      {/* Left Section */}
      <div
        className="hidden lg:flex w-1/2 relative bg-cover bg-center"
        style={{
          backgroundImage:
            "url('https://images.unsplash.com/photo-1500530855697-b586d89ba3ee')",
        }}
      >
        {/* Overlay */}
        <div className="absolute inset-0 bg-black/40" />

        {/* Menu */}
        <div className="absolute top-6 left-6 text-white flex items-center gap-2 z-10">
          <span className="text-xl">☰</span>
          <span className="font-medium">Menu</span>
        </div>

        {/* Logo */}
        <div className="absolute inset-0 flex items-center justify-center z-10">
          <h1 className="text-white text-3xl font-bold tracking-wide">
            ELBOD2i
          </h1>
        </div>

        {/* Social Icons */}
        <div className="absolute bottom-6 left-0 right-0 flex justify-center gap-4 z-10">
          <div className="social-icon"><FaFacebookF /></div>
          <div className="social-icon"><FaInstagram /></div>
          <div className="social-icon"><FaTwitter /></div>
        </div>
      </div>

      {/* Right Section */}
      <div className="w-full lg:w-1/2 flex items-center justify-center px-6">
        <div className="w-full max-w-md">
          <h2 className="text-3xl font-semibold text-gray-900 mb-2">
            Sign Up
          </h2>
          <p className="text-gray-500 mb-8">
            Please fill your information below
          </p>

          {/* Name */}
          <div className="mb-4">
            <label className="label">Name</label>
            <div className="input-wrapper">
              <FaUser className="input-icon" />
              <input
                type="text"
                placeholder="abc"
                className="input"
              />
            </div>
          </div>

          {/* Mobile */}
          <div className="mb-4">
            <label className="label">Mobile number</label>
            <div className="input-wrapper ring-2 ring-blue-400">
              <FaPhoneAlt className="input-icon" />
              <input
                type="text"
                placeholder="(91) 8767564357"
                className="input"
              />
            </div>
          </div>

          {/* Email */}
          <div className="mb-6">
            <label className="label">Email</label>
            <div className="input-wrapper">
              <FaEnvelope className="input-icon" />
              <input
                type="email"
                placeholder="abc@gmail.com"
                className="input"
              />
            </div>
          </div>

          {/* Button */}
          <button className="w-full bg-blue-600 hover:bg-blue-700 text-white py-3 rounded-lg flex items-center justify-center gap-2 transition">
            Next →
          </button>

          {/* Footer */}
          <div className="mt-6 text-sm text-gray-500 flex justify-between">
            <span>Already have an account</span>
            <a href="#" className="text-blue-600 font-medium hover:underline">
              Login to your account
            </a>
          </div>
        </div>
      </div>
    </div>
  );
}
